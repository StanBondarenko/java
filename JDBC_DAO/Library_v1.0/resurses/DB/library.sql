-- ============= CLEAN START =============
DROP TABLE IF EXISTS loan, book_copy, author_book, genre_book, reader, book, author, genre CASCADE;

-- ============= SCHEMA =============

-- Authors
CREATE TABLE author (
  author_id   serial PRIMARY KEY,
  first_name  varchar(50)  NOT NULL,
  last_name   varchar(100) NOT NULL,
  birthday    date         NOT NULL,
  death_day   date         NULL,
  CONSTRAINT chk_author_dates CHECK (
    birthday <= COALESCE(death_day, '9999-12-31') AND birthday <= CURRENT_DATE
  )
);

-- Genres
CREATE TABLE genre (
  genre_id    serial PRIMARY KEY,
  genre_name  varchar(50) NOT NULL,
  CONSTRAINT uq_genre_name UNIQUE (genre_name)
);

-- Books
CREATE TABLE book (
  book_id      serial PRIMARY KEY,
  title        varchar(100) NOT NULL,
  publish_date date         NOT NULL,
  count_stock  int          NOT NULL DEFAULT 0,
  CONSTRAINT chk_book_stock_nonneg CHECK (count_stock >= 0)
);

-- Readers
CREATE TABLE reader (
  reader_id    serial PRIMARY KEY,
  first_name   varchar(50)  NOT NULL,
  last_name    varchar(100) NOT NULL,
  address      varchar(100) NOT NULL,
  phone_number varchar(50)  NOT NULL,
  e_mail       varchar(254) NOT NULL
);
-- Case-insensitive uniqueness for emails
CREATE UNIQUE INDEX uq_reader_email_ci ON reader (lower(e_mail));

-- Book copies (physical inventory)
CREATE TABLE book_copy (
  copy_id        serial PRIMARY KEY,
  book_id        int NOT NULL REFERENCES book(book_id) ON DELETE CASCADE,
  inventory_code text UNIQUE
);

-- Loans
CREATE TABLE loan (
  loan_id     serial PRIMARY KEY,
  reader_id   int NOT NULL REFERENCES reader(reader_id)   ON DELETE RESTRICT,
  copy_id     int NOT NULL REFERENCES book_copy(copy_id)  ON DELETE RESTRICT,
  loaned_at   timestamptz NOT NULL DEFAULT now(),
  due_at      timestamptz NOT NULL,
  returned_at timestamptz NULL,
  CONSTRAINT chk_loan_dates CHECK (due_at > loaned_at),
  CONSTRAINT chk_return_after_loan CHECK (returned_at IS NULL OR returned_at >= loaned_at)
);

-- One active (unreturned) loan per copy
CREATE UNIQUE INDEX uq_active_loan_per_copy
  ON loan(copy_id) WHERE returned_at IS NULL;

-- M:N with cascading cleanup
CREATE TABLE genre_book (
  genre_id int NOT NULL,
  book_id  int NOT NULL,
  CONSTRAINT pk_genre_book PRIMARY KEY (genre_id, book_id),
  CONSTRAINT fk_genre_book_genre FOREIGN KEY (genre_id) REFERENCES genre(genre_id) ON DELETE CASCADE,
  CONSTRAINT fk_genre_book_book  FOREIGN KEY (book_id)  REFERENCES book(book_id)  ON DELETE CASCADE
);

CREATE TABLE author_book (
  author_id int NOT NULL,
  book_id   int NOT NULL,
  CONSTRAINT pk_author_book PRIMARY KEY (author_id, book_id),
  CONSTRAINT fk_author_book_author FOREIGN KEY (author_id) REFERENCES author(author_id) ON DELETE CASCADE,
  CONSTRAINT fk_author_book_book   FOREIGN KEY (book_id)   REFERENCES book(book_id)   ON DELETE CASCADE
);

-- Helpful indexes
CREATE INDEX idx_book_title       ON book (title);
CREATE INDEX idx_author_name      ON author (last_name, first_name);
CREATE INDEX idx_author_book_book ON author_book (book_id);
CREATE INDEX idx_genre_book_book  ON genre_book (book_id);

-- ============= RANDOM INVENTORY CODE (A–Z, len=8) =============

-- Random A–Z string generator
CREATE OR REPLACE FUNCTION random_inventory_code(n int DEFAULT 8)
RETURNS text AS $$
DECLARE
  chars  text := 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  result text := '';
  i int;
BEGIN
  FOR i IN 1..n LOOP
    result := result || substr(chars, floor(random()*26 + 1)::int, 1);
  END LOOP;
  RETURN result;
END;
$$ LANGUAGE plpgsql;

-- Ensure uniqueness by probing book_copy
CREATE OR REPLACE FUNCTION random_inventory_code_unique(n int DEFAULT 8)
RETURNS text AS $$
DECLARE
  candidate text;
  tries int := 0;
BEGIN
  LOOP
    candidate := random_inventory_code(n);
    PERFORM 1 FROM book_copy WHERE inventory_code = candidate;
    IF NOT FOUND THEN
      RETURN candidate;
    END IF;
    tries := tries + 1;
    IF tries > 50 THEN
      RAISE EXCEPTION 'Failed to generate unique inventory code after % tries', tries;
    END IF;
  END LOOP;
END;
$$ LANGUAGE plpgsql;

-- BEFORE INSERT trigger: fill inventory_code if NULL
CREATE OR REPLACE FUNCTION assign_random_inventory_code()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.inventory_code IS NULL THEN
    NEW.inventory_code := random_inventory_code_unique(8);
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_inventory_code ON book_copy;
CREATE TRIGGER trg_inventory_code
BEFORE INSERT ON book_copy
FOR EACH ROW
EXECUTE FUNCTION assign_random_inventory_code();

-- ============= SEED DATA =============
BEGIN;

-- ---- AUTHORS (15)
INSERT INTO author (first_name, last_name, birthday, death_day) VALUES
('Leo','Tolstoy','1828-09-09', '1910-11-20'),
('Fyodor','Dostoevsky','1821-11-11','1881-02-09'),
('Jane','Austen','1775-12-16','1817-07-18'),
('Mark','Twain','1835-11-30','1910-04-21'),
('George','Orwell','1903-06-25','1950-01-21'),
('Harper','Lee','1926-04-28','2016-02-19'),
('J.K.','Rowling','1965-07-31',NULL),
('J.R.R.','Tolkien','1892-01-03','1973-09-02'),
('Agatha','Christie','1890-09-15','1976-01-12'),
('Arthur','Conan Doyle','1859-05-22','1930-07-07'),
('Ernest','Hemingway','1899-07-21','1961-07-02'),
('Mary','Shelley','1797-08-30','1851-02-01'),
('Herman','Melville','1819-08-01','1891-09-28'),
('Gabriel','Garcia Marquez','1927-03-06','2014-04-17'),
('Virginia','Woolf','1882-01-25','1941-03-28');

-- ---- GENRES (15)
INSERT INTO genre (genre_name) VALUES
('Classic'),('Novel'),('Fantasy'),('Mystery'),('Detective'),
('Science Fiction'),('Horror'),('Historical'),('Satire'),('Drama'),
('Adventure'),('Philosophical'),('Romance'),('Magical Realism'),('Short Stories');

-- ---- BOOKS (15)
INSERT INTO book (title, publish_date, count_stock) VALUES
('War and Peace',      '1869-01-01', 2),
('Crime and Punishment','1866-01-01',1),
('Pride and Prejudice','1813-01-28', 1),
('Adventures of Huckleberry Finn','1884-12-10',1),
('1984','1949-06-08', 2),
('To Kill a Mockingbird','1960-07-11',1),
('Harry Potter and the Sorcerer''s Stone','1997-06-26',2),
('The Hobbit','1937-09-21', 2),
('Murder on the Orient Express','1934-01-01',1),
('The Hound of the Baskervilles','1902-04-01',1),
('The Old Man and the Sea','1952-09-01',1),
('Frankenstein','1818-01-01',1),
('Moby-Dick','1851-10-18',1),
('One Hundred Years of Solitude','1967-05-30',1),
('Mrs Dalloway','1925-05-14',1);

-- ---- READERS (20)
INSERT INTO reader (first_name, last_name, address, phone_number, e_mail) VALUES
('Alice','Brown','12 Pine St','+1-212-000-0001','alice.brown@example.com'),
('Bob','Smith','34 Oak Ave','+1-212-000-0002','bob.smith@example.com'),
('Carol','Johnson','56 Maple Rd','+1-212-000-0003','carol.j@example.com'),
('David','Wilson','78 Birch Ln','+1-212-000-0004','david.w@example.com'),
('Eve','Davis','90 Cedar St','+1-212-000-0005','eve.d@example.com'),
('Frank','Miller','11 Elm St','+1-212-000-0006','frank.m@example.com'),
('Grace','Taylor','22 Spruce Ave','+1-212-000-0007','grace.t@example.com'),
('Heidi','Anderson','33 Walnut Rd','+1-212-000-0008','heidi.a@example.com'),
('Ivan','Thomas','44 Cherry Ln','+1-212-000-0009','ivan.t@example.com'),
('Judy','Jackson','55 Ash St','+1-212-000-0010','judy.j@example.com'),
('Ken','White','66 Poplar Ave','+1-212-000-0011','ken.w@example.com'),
('Laura','Harris','77 Willow Rd','+1-212-000-0012','laura.h@example.com'),
('Mallory','Martin','88 Cypress Ln','+1-212-000-0013','mallory.m@example.com'),
('Niaj','Thompson','99 Palm St','+1-212-000-0014','niaj.t@example.com'),
('Olivia','Garcia','101 Vine Ave','+1-212-000-0015','olivia.g@example.com'),
('Peggy','Martinez','202 Ivy Rd','+1-212-000-0016','peggy.m@example.com'),
('Quentin','Robinson','303 Fern Ln','+1-212-000-0017','quentin.r@example.com'),
('Ruth','Clark','404 Lilac St','+1-212-000-0018','ruth.c@example.com'),
('Sybil','Rodriguez','505 Lotus Ave','+1-212-000-0019','sybil.r@example.com'),
('Trent','Lewis','606 Orchid Rd','+1-212-000-0020','trent.l@example.com');

-- ---- AUTHOR_BOOK links
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='War and Peace' WHERE a.last_name='Tolstoy';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Crime and Punishment' WHERE a.last_name='Dostoevsky';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Pride and Prejudice' WHERE a.last_name='Austen';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Adventures of Huckleberry Finn' WHERE a.last_name='Twain';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='1984' WHERE a.last_name='Orwell';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='To Kill a Mockingbird' WHERE a.last_name='Lee';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Harry Potter and the Sorcerer''s Stone' WHERE a.last_name='Rowling';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='The Hobbit' WHERE a.last_name='Tolkien';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Murder on the Orient Express' WHERE a.last_name='Christie';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='The Hound of the Baskervilles' WHERE a.last_name='Conan Doyle';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='The Old Man and the Sea' WHERE a.last_name='Hemingway';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Frankenstein' WHERE a.last_name='Shelley' AND a.first_name='Mary';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Moby-Dick' WHERE a.last_name='Melville';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='One Hundred Years of Solitude' WHERE a.last_name='Garcia Marquez';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Mrs Dalloway' WHERE a.last_name='Woolf';

-- доп. связи для разнообразия
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='The Old Man and the Sea' WHERE a.last_name='Twain';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Murder on the Orient Express' WHERE a.last_name='Conan Doyle';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Frankenstein' WHERE a.last_name='Orwell';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='The Hound of the Baskervilles' WHERE a.last_name='Christie';

-- ---- GENRE_BOOK links
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='War and Peace' WHERE g.genre_name IN ('Classic','Historical','Drama');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Crime and Punishment' WHERE g.genre_name IN ('Classic','Philosophical');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Pride and Prejudice' WHERE g.genre_name IN ('Classic','Romance');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Adventures of Huckleberry Finn' WHERE g.genre_name IN ('Classic','Adventure');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='1984' WHERE g.genre_name IN ('Classic','Science Fiction','Satire');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='To Kill a Mockingbird' WHERE g.genre_name IN ('Classic','Drama');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Harry Potter and the Sorcerer''s Stone' WHERE g.genre_name IN ('Fantasy','Adventure');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='The Hobbit' WHERE g.genre_name IN ('Fantasy','Adventure');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Murder on the Orient Express' WHERE g.genre_name IN ('Mystery','Detective');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='The Hound of the Baskervilles' WHERE g.genre_name IN ('Mystery','Detective');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='The Old Man and the Sea' WHERE g.genre_name IN ('Classic','Drama');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Frankenstein' WHERE g.genre_name IN ('Horror','Science Fiction');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Moby-Dick' WHERE g.genre_name IN ('Classic','Adventure');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='One Hundred Years of Solitude' WHERE g.genre_name IN ('Classic','Magical Realism');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Mrs Dalloway' WHERE g.genre_name IN ('Classic','Novel');

-- ---- BOOK_COPY (20 копий: базово 15 + 5 дополнительных)
-- базовые 15 (по одной на книгу)
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='War and Peace';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Crime and Punishment';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Pride and Prejudice';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Adventures of Huckleberry Finn';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='1984';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='To Kill a Mockingbird';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Harry Potter and the Sorcerer''s Stone';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='The Hobbit';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Murder on the Orient Express';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='The Hound of the Baskervilles';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='The Old Man and the Sea';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Frankenstein';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Moby-Dick';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='One Hundred Years of Solitude';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Mrs Dalloway';

-- дополнительные 5 (для книг со stock=2; и третью для War and Peace)
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='War and Peace';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='1984';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Harry Potter and the Sorcerer''s Stone';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='The Hobbit';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='War and Peace'; -- третья копия

-- ---- LOANS (18 шт.) — выбор свободной копии по названию книги

-- helper CTE для окна вставок: нет, вставляем напрямую

-- 1
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='alice.brown@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='War and Peace'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-01 10:00+00','2025-08-15 10:00+00','2025-08-12 16:00+00'
);

-- 2
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='bob.smith@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='1984'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-03 09:00+00','2025-08-17 09:00+00',NULL
);

-- 3
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='carol.j@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='Pride and Prejudice'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-05 11:30+00','2025-08-19 11:30+00','2025-08-18 14:00+00'
);

-- 4
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='david.w@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='The Hobbit'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-07 15:00+00','2025-08-21 15:00+00',NULL
);

-- 5
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='eve.d@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='Harry Potter and the Sorcerer''s Stone'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-09 13:00+00','2025-08-23 13:00+00',NULL
);

-- 6
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='frank.m@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='Adventures of Huckleberry Finn'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-10 09:15+00','2025-08-24 09:15+00','2025-08-20 10:00+00'
);

-- 7
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='grace.t@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='Murder on the Orient Express'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-10 10:00+00','2025-08-24 10:00+00','2025-08-22 12:00+00'
);

-- 8
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='heidi.a@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='The Hound of the Baskervilles'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-11 08:00+00','2025-08-25 08:00+00',NULL
);

-- 9
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='ivan.t@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='The Old Man and the Sea'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-12 14:30+00','2025-08-26 14:30+00','2025-08-26 13:00+00'
);

-- 10
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='judy.j@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='Frankenstein'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-12 16:00+00','2025-08-26 16:00+00',NULL
);

-- 11
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='ken.w@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='Moby-Dick'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-13 10:00+00','2025-08-27 10:00+00','2025-08-27 09:30+00'
);

-- 12
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='laura.h@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='One Hundred Years of Solitude'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-14 11:00+00','2025-08-28 11:00+00',NULL
);

-- 13
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='mallory.m@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='Mrs Dalloway'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-15 12:00+00','2025-08-29 12:00+00','2025-08-28 18:00+00'
);

-- 14
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='niaj.t@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='War and Peace'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-16 09:45+00','2025-08-30 09:45+00',NULL
);

-- 15
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='olivia.g@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='1984'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-17 10:30+00','2025-08-31 10:30+00','2025-08-29 17:00+00'
);

-- 16
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='peggy.m@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='Harry Potter and the Sorcerer''s Stone'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-18 13:00+00','2025-09-01 13:00+00',NULL
);

-- 17
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='quentin.r@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='The Hobbit'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-19 14:15+00','2025-09-02 14:15+00','2025-09-01 10:00+00'
);

-- 18
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='ruth.c@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='War and Peace'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-20 09:00+00','2025-09-03 09:00+00',NULL
);

COMMIT;

-- ---- ДОБАВОЧНЫЙ ПАКЕТ КНИГ (ещё 15) ----
BEGIN;

INSERT INTO book (title, publish_date, count_stock) VALUES
('Anna Karenina', '1877-01-01', 1),
('The Brothers Karamazov', '1880-01-01', 1),
('Sense and Sensibility', '1811-10-30', 1),
('The Adventures of Tom Sawyer', '1876-06-01', 1),
('Animal Farm', '1945-08-17', 1),
('Go Set a Watchman', '2015-07-14', 1),
('Harry Potter and the Chamber of Secrets', '1998-07-02', 1),
('The Fellowship of the Ring', '1954-07-29', 1),
('And Then There Were None', '1939-11-06', 1),
('A Study in Scarlet', '1887-11-01', 1),
('A Farewell to Arms', '1929-09-27', 1),
('The Last Man', '1826-01-01', 1),
('Billy Budd', '1924-01-01', 1),
('Love in the Time of Cholera', '1985-03-05', 1),
('To the Lighthouse', '1927-05-05', 1);

-- link to authors
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Anna Karenina' WHERE a.last_name='Tolstoy';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='The Brothers Karamazov' WHERE a.last_name='Dostoevsky';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Sense and Sensibility' WHERE a.last_name='Austen';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='The Adventures of Tom Sawyer' WHERE a.last_name='Twain';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Animal Farm' WHERE a.last_name='Orwell';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Go Set a Watchman' WHERE a.last_name='Lee';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Harry Potter and the Chamber of Secrets' WHERE a.last_name='Rowling';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='The Fellowship of the Ring' WHERE a.last_name='Tolkien';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='And Then There Were None' WHERE a.last_name='Christie';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='A Study in Scarlet' WHERE a.last_name='Conan Doyle';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='A Farewell to Arms' WHERE a.last_name='Hemingway';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='The Last Man' WHERE a.last_name='Shelley' AND a.first_name='Mary';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Billy Budd' WHERE a.last_name='Melville';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Love in the Time of Cholera' WHERE a.last_name='Garcia Marquez';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='To the Lighthouse' WHERE a.last_name='Woolf';

-- genre links
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Anna Karenina' WHERE g.genre_name IN ('Classic','Romance');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='The Brothers Karamazov' WHERE g.genre_name IN ('Classic','Philosophical');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Sense and Sensibility' WHERE g.genre_name IN ('Classic','Romance');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='The Adventures of Tom Sawyer' WHERE g.genre_name IN ('Classic','Adventure');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Animal Farm' WHERE g.genre_name IN ('Classic','Satire','Science Fiction');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Go Set a Watchman' WHERE g.genre_name IN ('Novel','Drama');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Harry Potter and the Chamber of Secrets' WHERE g.genre_name IN ('Fantasy','Adventure');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='The Fellowship of the Ring' WHERE g.genre_name IN ('Fantasy','Adventure');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='And Then There Were None' WHERE g.genre_name IN ('Mystery','Detective');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='A Study in Scarlet' WHERE g.genre_name IN ('Mystery','Detective');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='A Farewell to Arms' WHERE g.genre_name IN ('Classic','Historical','Drama');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='The Last Man' WHERE g.genre_name IN ('Horror','Science Fiction');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Billy Budd' WHERE g.genre_name IN ('Classic','Novel');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Love in the Time of Cholera' WHERE g.genre_name IN ('Classic','Romance','Magical Realism');
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='To the Lighthouse' WHERE g.genre_name IN ('Classic','Novel');

-- one copy per each new book (codes auto by trigger)
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Anna Karenina';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='The Brothers Karamazov';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Sense and Sensibility';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='The Adventures of Tom Sawyer';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Animal Farm';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Go Set a Watchman';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Harry Potter and the Chamber of Secrets';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='The Fellowship of the Ring';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='And Then There Were None';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='A Study in Scarlet';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='A Farewell to Arms';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='The Last Man';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Billy Budd';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='Love in the Time of Cholera';
INSERT INTO book_copy (book_id) SELECT book_id FROM book WHERE title='To the Lighthouse';

COMMIT;

-- ---- СИНХРОНИЗАЦИЯ count_stock ПО ФАКТУ ----
UPDATE book b
SET count_stock = c.cnt
FROM (SELECT book_id, COUNT(*) AS cnt FROM book_copy GROUP BY book_id) c
WHERE b.book_id = c.book_id;
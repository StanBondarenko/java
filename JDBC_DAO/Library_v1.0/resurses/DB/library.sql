-- DELETE
DROP TABLE IF EXISTS loan, book_copy, author_book, genre_book, reader, book, author, genre CASCADE;

-- Author
CREATE TABLE author (
  author_id   serial PRIMARY KEY,
  first_name  varchar(50)  NOT NULL,
  last_name   varchar(100) NOT NULL,
  birthday    date         NOT NULL,
  death_day   date         NULL
);

-- Genre
CREATE TABLE genre (
  genre_id    serial PRIMARY KEY,
  genre_name  varchar(50) NOT NULL
);

-- books
CREATE TABLE book (
  book_id      serial PRIMARY KEY,
  title        varchar(100) NOT NULL,
  publish_date date         NOT NULL,
  count_stock  int          NOT NULL DEFAULT 0
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

-- Book copies (physical inventory)
CREATE TABLE book_copy (
  copy_id        serial PRIMARY KEY,
  book_id        int NOT NULL REFERENCES book(book_id),
  inventory_code text
);

-- Loans
CREATE TABLE loan (
  loan_id     serial PRIMARY KEY,
  reader_id   int NOT NULL REFERENCES reader(reader_id),
  copy_id     int NOT NULL REFERENCES book_copy(copy_id),
  loaned_at   timestamptz NOT NULL,
  due_at      timestamptz NOT NULL,
  returned_at timestamptz NULL
);

-- Accosiative
CREATE TABLE genre_book (
  genre_id int NOT NULL,
  book_id  int NOT NULL,
  PRIMARY KEY (genre_id, book_id),
  FOREIGN KEY (genre_id) REFERENCES genre(genre_id),
  FOREIGN KEY (book_id)  REFERENCES book(book_id)
);

CREATE TABLE author_book (
  author_id int NOT NULL,
  book_id   int NOT NULL,
  PRIMARY KEY (author_id, book_id),
  FOREIGN KEY (author_id) REFERENCES author(author_id),
  FOREIGN KEY (book_id)   REFERENCES book(book_id)
);

-- ================== SIMPLE FUNCTION: random numeric code ==================
CREATE OR REPLACE FUNCTION random_inventory_number(n int DEFAULT 8)
RETURNS text AS $$
DECLARE
  result text := '';
  i int;
BEGIN
  -- складываем n случайных цифр
  FOR i IN 1..n LOOP
    result := result || trunc(random()*10)::int;
  END LOOP;
  RETURN result;
END;
$$ LANGUAGE plpgsql;

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

-- ---- BOOK_COPY (теперь сразу с инвентарными кодами)
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='War and Peace';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Crime and Punishment';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Pride and Prejudice';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Adventures of Huckleberry Finn';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='1984';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='To Kill a Mockingbird';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Harry Potter and the Sorcerer''s Stone';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='The Hobbit';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Murder on the Orient Express';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='The Hound of the Baskervilles';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='The Old Man and the Sea';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Frankenstein';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Moby-Dick';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='One Hundred Years of Solitude';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Mrs Dalloway';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='War and Peace';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='1984';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Harry Potter and the Sorcerer''s Stone';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='The Hobbit';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='War and Peace'; -- третья копия

-- 1..18 LOANS (без изменений)
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='alice.brown@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='War and Peace'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-01 10:00+00','2025-08-15 10:00+00','2025-08-12 16:00+00'
);

INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='bob.smith@example.com'),
  (SELECT bc.copy_id FROM book_copy bc JOIN book b ON b.book_id=bc.book_id
   WHERE b.title='1984'
     AND NOT EXISTS (SELECT 1 FROM loan l WHERE l.copy_id=bc.copy_id AND l.returned_at IS NULL)
   ORDER BY bc.copy_id LIMIT 1),
  '2025-08-03 09:00+00','2025-08-17 09:00+00',NULL
);

-- ... (оставшиеся INSERT INTO loan из твоего блока без изменений)
-- Чтобы не раздувать ответ, вставь сюда остальные твои 16 вставок loan как есть.

COMMIT;

--- More data
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

-- author links (как у тебя)
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='Anna Karenina' WHERE a.last_name='Tolstoy';
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id FROM author a JOIN book b ON b.title='The Brothers Karamazov' WHERE a.last_name='Dostoevsky';
-- ... (остальные 13 вставок author_book без изменений)

-- genre links (как у тебя)
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Anna Karenina' WHERE g.genre_name IN ('Classic','Romance');
-- ... (остальные 14 вставок genre_book без изменений)

-- one copy per each new book (теперь без триггера: вызываем функцию)
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Anna Karenina';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='The Brothers Karamazov';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Sense and Sensibility';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='The Adventures of Tom Sawyer';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Animal Farm';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Go Set a Watchman';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Harry Potter and the Chamber of Secrets';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='The Fellowship of the Ring';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='And Then There Were None';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='A Study in Scarlet';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='A Farewell to Arms';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='The Last Man';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Billy Budd';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='Love in the Time of Cholera';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, random_inventory_number(8) FROM book WHERE title='To the Lighthouse';

COMMIT;

-- ---- СИНХРОНИЗАЦИЯ count_stock ПО ФАКТУ ----
UPDATE book b
SET count_stock = c.cnt
FROM (SELECT book_id, COUNT(*) AS cnt FROM book_copy GROUP BY book_id) c
WHERE b.book_id = c.book_id;
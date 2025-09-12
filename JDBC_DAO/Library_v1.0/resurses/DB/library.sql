DROP TABLE IF EXISTS loan, book_copy, author_book, genre_book, reader, book, author, genre CASCADE;

-- ---------- Core reference tables ----------

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

CREATE TABLE genre (
  genre_id    serial PRIMARY KEY,
  genre_name  varchar(50) NOT NULL,
  CONSTRAINT uq_genre_name UNIQUE (genre_name)
);

CREATE TABLE book (
  book_id      serial PRIMARY KEY,
  title        varchar(100) NOT NULL,
  publish_date date         NOT NULL,
  count_stock  int          NOT NULL DEFAULT 0,
  CONSTRAINT chk_book_stock_nonneg CHECK (count_stock >= 0)
);

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

-- ---------- Inventory (per-physical-copy) ----------

CREATE TABLE book_copy (
  copy_id        serial PRIMARY KEY,
  book_id        int NOT NULL REFERENCES book(book_id) ON DELETE CASCADE,
  inventory_code text UNIQUE
);

-- ---------- Loans (borrowing) ----------

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

-- One active (unreturned) loan per physical copy
CREATE UNIQUE INDEX uq_active_loan_per_copy
  ON loan(copy_id) WHERE returned_at IS NULL;

-- ---------- M:N relations with cascading cleanup ----------

CREATE TABLE genre_book (
  genre_id int NOT NULL,
  book_id  int NOT NULL,
  CONSTRAINT pk_genre_book PRIMARY KEY (genre_id, book_id),
  CONSTRAINT fk_genre_book_genre FOREIGN KEY (genre_id) REFERENCES genre(genre_id) ON DELETE CASCADE,
  CONSTRAINT fk_genre_book_book  FOREIGN KEY (book_id)  REFERENCES book(book_id)   ON DELETE CASCADE
);

CREATE TABLE author_book (
  author_id int NOT NULL,
  book_id   int NOT NULL,
  CONSTRAINT pk_author_book PRIMARY KEY (author_id, book_id),
  CONSTRAINT fk_author_book_author FOREIGN KEY (author_id) REFERENCES author(author_id) ON DELETE CASCADE,
  CONSTRAINT fk_author_book_book   FOREIGN KEY (book_id)   REFERENCES book(book_id)    ON DELETE CASCADE
);

-- ---------- Helpful indexes (optional but practical) ----------

-- Search by book title
CREATE INDEX idx_book_title ON book (title);

-- Search authors by name
CREATE INDEX idx_author_name ON author (last_name, first_name);

-- Fast lookups for junctions
CREATE INDEX idx_author_book_book  ON author_book (book_id);
CREATE INDEX idx_genre_book_book   ON genre_book  (book_id);

BEGIN;

-- -------------------------
-- AUTHORS (15)
-- -------------------------
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

-- -------------------------
-- GENRES (15, UNIQUE NAMES)
-- -------------------------
INSERT INTO genre (genre_name) VALUES
('Classic'),('Novel'),('Fantasy'),('Mystery'),('Detective'),
('Science Fiction'),('Horror'),('Historical'),('Satire'),('Drama'),
('Adventure'),('Philosophical'),('Romance'),('Magical Realism'),('Short Stories');

-- -------------------------
-- BOOKS (15)
-- count_stock выставлен равным числу копий, которые создадим в book_copy
-- -------------------------
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

-- -------------------------
-- READERS (20) — уникальные email (CI-уникальность обеспечит индекс lower(e_mail))
-- -------------------------
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

-- -------------------------
-- AUTHOR_BOOK (≈19 связей: у некоторых книг по 2 автора)
-- привязка по названиям/именам, чтобы не зависеть от id
-- -------------------------
-- Tolstoy -> War and Peace
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='War and Peace'
WHERE a.last_name='Tolstoy';

-- Dostoevsky -> Crime and Punishment
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='Crime and Punishment'
WHERE a.last_name='Dostoevsky';

-- Austen -> Pride and Prejudice
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='Pride and Prejudice'
WHERE a.last_name='Austen';

-- Twain -> Huckleberry Finn
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='Adventures of Huckleberry Finn'
WHERE a.last_name='Twain';

-- Orwell -> 1984
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='1984'
WHERE a.last_name='Orwell';

-- Lee -> To Kill a Mockingbird
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='To Kill a Mockingbird'
WHERE a.last_name='Lee';

-- Rowling -> HP1
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='Harry Potter and the Sorcerer''s Stone'
WHERE a.last_name='Rowling';

-- Tolkien -> The Hobbit
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='The Hobbit'
WHERE a.last_name='Tolkien';

-- Christie -> Murder on the Orient Express
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='Murder on the Orient Express'
WHERE a.last_name='Christie';

-- Conan Doyle -> Hound of the Baskervilles
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='The Hound of the Baskervilles'
WHERE a.last_name='Conan Doyle';

-- Hemingway -> Old Man and the Sea
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='The Old Man and the Sea'
WHERE a.last_name='Hemingway';

-- Mary Shelley -> Frankenstein
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='Frankenstein'
WHERE a.last_name='Shelley' AND a.first_name='Mary';

-- Melville -> Moby-Dick
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='Moby-Dick'
WHERE a.last_name='Melville';

-- Garcia Marquez -> One Hundred Years of Solitude
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='One Hundred Years of Solitude'
WHERE a.last_name='Garcia Marquez';

-- Woolf -> Mrs Dalloway
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='Mrs Dalloway'
WHERE a.last_name='Woolf';

-- Дополнительные «соавторы» (для разнообразия связей)
-- Twain + 2-я связь к "The Old Man and the Sea" (условная редакция/вступление)
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='The Old Man and the Sea'
WHERE a.last_name='Twain';

-- Conan Doyle + 2-я связь к "Murder on the Orient Express" (условный комментарий)
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='Murder on the Orient Express'
WHERE a.last_name='Conan Doyle';

-- Orwell + 2-я связь к "Animal Farm" (книги нет — пропустим). Вместо этого добавим к "Frankenstein"
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='Frankenstein'
WHERE a.last_name='Orwell';

-- Christie + 2-я связь к "Hound of the Baskervilles"
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a JOIN book b ON b.title='The Hound of the Baskervilles'
WHERE a.last_name='Christie';

-- -------------------------
-- GENRE_BOOK (≈20 связей; 1–2 жанра на книгу)
-- -------------------------
-- Helper: функция-вставка через SELECT
-- Classic, Novel, Fantasy, Mystery, Detective, SciFi, Horror, Historical, Satire, Drama, Adventure, Philosophical, Romance, Magical Realism
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='War and Peace' WHERE g.genre_name IN ('Classic','Historical');

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

-- Добавим ещё несколько вторых жанров для набора ~20 связей
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='War and Peace' WHERE g.genre_name='Drama';

INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id FROM genre g JOIN book b ON b.title='Moby-Dick' WHERE g.genre_name='Philosophical';

-- -------------------------
-- BOOK_COPY (20 копий на 15 книг: у каждой минимум 1, у пяти книг по 2)
-- inventory_code уникален, читаемый
-- -------------------------
-- Для краткости: по каждой книге вставим 1 копию, а для пяти — вторую.
-- 1) Базовые 15 копий (по одной)
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'WAR-001' FROM book WHERE title='War and Peace';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'CRP-001' FROM book WHERE title='Crime and Punishment';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'PRP-001' FROM book WHERE title='Pride and Prejudice';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'HUC-001' FROM book WHERE title='Adventures of Huckleberry Finn';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'NIN-001' FROM book WHERE title='1984';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'TKM-001' FROM book WHERE title='To Kill a Mockingbird';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'HP1-001' FROM book WHERE title='Harry Potter and the Sorcerer''s Stone';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'HOB-001' FROM book WHERE title='The Hobbit';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'MOR-001' FROM book WHERE title='Murder on the Orient Express';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'HND-001' FROM book WHERE title='The Hound of the Baskervilles';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'OMS-001' FROM book WHERE title='The Old Man and the Sea';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'FRK-001' FROM book WHERE title='Frankenstein';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'MOB-001' FROM book WHERE title='Moby-Dick';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'OHS-001' FROM book WHERE title='One Hundred Years of Solitude';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'MSD-001' FROM book WHERE title='Mrs Dalloway';

-- 2) Дополнительные 5 вторых копий для книг, где count_stock=2
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'WAR-002' FROM book WHERE title='War and Peace';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'NIN-002' FROM book WHERE title='1984';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'HP1-002' FROM book WHERE title='Harry Potter and the Sorcerer''s Stone';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'HOB-002' FROM book WHERE title='The Hobbit';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'WAR-003' FROM book WHERE title='War and Peace'; -- у War and Peace в book.count_stock = 2, но копий 3 — допустимо (излишек на будущее)

-- Опционально синхронизировать count_stock = фактическому числу копий (если хочешь строгую согласованность)
-- UPDATE book b SET count_stock = c.cnt
-- FROM (SELECT book_id, COUNT(*) AS cnt FROM book_copy GROUP BY book_id) c
-- WHERE b.book_id = c.book_id;

-- -------------------------
-- LOANS (18 шт.) — даты согласованы, активных выдач несколько; ни одна копия не выдана одновременно двум людям
-- -------------------------
-- Хелпер для выборки reader_id по email:
-- SELECT reader_id FROM reader WHERE e_mail='...'
-- Хелпер для выборки copy_id по inventory_code:
-- SELECT copy_id FROM book_copy WHERE inventory_code='...'

-- 1. Alice берет WAR-001
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='alice.brown@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='WAR-001'),
  '2025-08-01 10:00+00','2025-08-15 10:00+00','2025-08-12 16:00+00'
);

-- 2. Bob берет NIN-001 (1984) — активная
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='bob.smith@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='NIN-001'),
  '2025-08-03 09:00+00','2025-08-17 09:00+00',NULL
);

-- 3. Carol берет PRP-001
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='carol.j@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='PRP-001'),
  '2025-08-05 11:30+00','2025-08-19 11:30+00','2025-08-18 14:00+00'
);

-- 4. David берет HOB-002 — активная
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='david.w@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='HOB-002'),
  '2025-08-07 15:00+00','2025-08-21 15:00+00',NULL
);

-- 5. Eve берет HP1-002 — активная
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='eve.d@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='HP1-002'),
  '2025-08-09 13:00+00','2025-08-23 13:00+00',NULL
);

-- 6. Frank берет HUC-001 (Huckleberry Finn)
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='frank.m@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='HUC-001'),
  '2025-08-10 09:15+00','2025-08-24 09:15+00','2025-08-20 10:00+00'
);

-- 7. Grace берет MOR-001 (Orient Express)
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='grace.t@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='MOR-001'),
  '2025-08-10 10:00+00','2025-08-24 10:00+00','2025-08-22 12:00+00'
);

-- 8. Heidi берет HND-001 — активная
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='heidi.a@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='HND-001'),
  '2025-08-11 08:00+00','2025-08-25 08:00+00',NULL
);

-- 9. Ivan берет OMS-001
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='ivan.t@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='OMS-001'),
  '2025-08-12 14:30+00','2025-08-26 14:30+00','2025-08-26 13:00+00'
);

-- 10. Judy берет FRK-001 — активная
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='judy.j@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='FRK-001'),
  '2025-08-12 16:00+00','2025-08-26 16:00+00',NULL
);

-- 11. Ken берет MOB-001
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='ken.w@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='MOB-001'),
  '2025-08-13 10:00+00','2025-08-27 10:00+00','2025-08-27 09:30+00'
);

-- 12. Laura берет OHS-001 — активная
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='laura.h@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='OHS-001'),
  '2025-08-14 11:00+00','2025-08-28 11:00+00',NULL
);

-- 13. Mallory берет MSD-001
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='mallory.m@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='MSD-001'),
  '2025-08-15 12:00+00','2025-08-29 12:00+00','2025-08-28 18:00+00'
);

-- 14. Niaj берет WAR-002 — активная
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='niaj.t@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='WAR-002'),
  '2025-08-16 09:45+00','2025-08-30 09:45+00',NULL
);

-- 15. Olivia берет NIN-002
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='olivia.g@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='NIN-002'),
  '2025-08-17 10:30+00','2025-08-31 10:30+00','2025-08-29 17:00+00'
);

-- 16. Peggy берет HP1-001 — активная
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='peggy.m@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='HP1-001'),
  '2025-08-18 13:00+00','2025-09-01 13:00+00',NULL
);

-- 17. Quentin берет HOB-001
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='quentin.r@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='HOB-001'),
  '2025-08-19 14:15+00','2025-09-02 14:15+00','2025-09-01 10:00+00'
);

-- 18. Ruth берет WAR-003 — активная (третья копия)
INSERT INTO loan (reader_id, copy_id, loaned_at, due_at, returned_at)
VALUES (
  (SELECT reader_id FROM reader WHERE e_mail='ruth.c@example.com'),
  (SELECT copy_id FROM book_copy WHERE inventory_code='WAR-003'),
  '2025-08-20 09:00+00','2025-09-03 09:00+00',NULL
);

COMMIT;
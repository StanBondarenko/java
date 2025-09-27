DROP TABLE IF EXISTS book_copy, author_book, genre_book, reader, book, author, genre CASCADE;

CREATE TABLE author (
  author_id   serial PRIMARY KEY,
  first_name  varchar(50)  NOT NULL,
  last_name   varchar(100) NOT NULL,
  birthday    date         NOT NULL,
  death_day   date         NULL
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

CREATE TABLE book_copy (
  copy_id        serial PRIMARY KEY,
  book_id        int NOT NULL REFERENCES book(book_id),
  inventory_code text UNIQUE
);

CREATE TABLE author_book (
  author_id int NOT NULL,
  book_id   int NOT NULL,
  CONSTRAINT pk_author_book PRIMARY KEY (author_id, book_id),
  CONSTRAINT fk_author_book_author FOREIGN KEY (author_id) REFERENCES author(author_id),
  CONSTRAINT fk_author_book_book   FOREIGN KEY (book_id)   REFERENCES book(book_id)
  );

CREATE TABLE genre_book (
  genre_id int NOT NULL,
  book_id  int NOT NULL,
  CONSTRAINT pk_genre_book PRIMARY KEY (genre_id, book_id),
  CONSTRAINT fk_genre_book_genre FOREIGN KEY (genre_id) REFERENCES genre(genre_id),
  CONSTRAINT fk_genre_book_book  FOREIGN KEY (book_id)  REFERENCES book(book_id) 
  );

----Data
BEGIN;

-- AUTHORS
INSERT INTO author (first_name, last_name, birthday, death_day) VALUES
('TestAuthorA_First','TestAuthorA_Last','1970-01-01',NULL),
('TestAuthorB_First','TestAuthorB_Last','1980-02-02',NULL),
('TestAuthorC_First','TestAuthorC_Last','1990-03-03',NULL),
('TestAuthorD_First','TestAuthorD_Last','1965-04-04',NULL);

-- GENRES 
INSERT INTO genre (genre_name) VALUES
('TestGenre1'),
('TestGenre2'),
('TestGenre3'),
('TestGenre4');

INSERT INTO book (title, publish_date, count_stock) VALUES
('TestBook1','2001-01-01',2),
('TestBook2','2002-02-02',1),
('TestBook3','2003-03-03',1),
('TestBook4','2004-04-04',0);

-- READERS 
INSERT INTO reader (first_name, last_name, address, phone_number, e_mail) VALUES
('TestReader1_First','TestReader1_Last','Addr 1','+1-555-0001','testreader1@example.com'),
('TestReader2_First','TestReader2_Last','Addr 2','+1-555-0002','testreader2@example.com'),
('TestReader3_First','TestReader3_Last','Addr 3','+1-555-0003','testreader3@example.com'),
('TestReader4_First','TestReader4_Last','Addr 4','+1-555-0004','testreader4@example.com');

-- AUTHOR_BOOK (4) — 1 автор на каждую книгу
INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a, book b
WHERE a.last_name='TestAuthorA_Last' AND b.title='TestBook1';

INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a, book b
WHERE a.last_name='TestAuthorB_Last' AND b.title='TestBook2';

INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a, book b
WHERE a.last_name='TestAuthorC_Last' AND b.title='TestBook3';

INSERT INTO author_book (author_id, book_id)
SELECT a.author_id, b.book_id
FROM author a, book b
WHERE a.last_name='TestAuthorD_Last' AND b.title='TestBook4';

-- GENRE_BOOK (4) — 1 жанр на каждую книгу
INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id
FROM genre g, book b
WHERE g.genre_name='TestGenre1' AND b.title='TestBook1';

INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id
FROM genre g, book b
WHERE g.genre_name='TestGenre2' AND b.title='TestBook2';

INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id
FROM genre g, book b
WHERE g.genre_name='TestGenre3' AND b.title='TestBook3';

INSERT INTO genre_book (genre_id, book_id)
SELECT g.genre_id, b.book_id
FROM genre g, book b
WHERE g.genre_name='TestGenre4' AND b.title='TestBook4';


-- Book1: 2 copies
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'TEST1-A' FROM book WHERE title='TestBook1';
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'TEST1-B' FROM book WHERE title='TestBook1';

-- Book2: 1 copy
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'TEST2-A' FROM book WHERE title='TestBook2';

-- Book3: 1 copy
INSERT INTO book_copy (book_id, inventory_code)
SELECT book_id, 'TEST3-A' FROM book WHERE title='TestBook3';

COMMIT;

SELECT *
	FROM book

SELECT *
	FROM author
SELECT *
	FROM loan

SELECT *
	FROM author_book
SELECT genre_id, book_id, title,genre_name
	FROM genre_book
	JOIN book USING(book_id)
	JOIN genre USING(genre_id)
	WHERE book_id = 33
SELECT *
	FROM genre
SELECT title, genre_name, count_stock, (author_name ||'' )
	FROM book
	JOIN genre_book USING(book_id)
	JOIN genre USING(genre_id)
	WHERE genre_name ILIKE '%my%'

SELECT copy_id, book_id, inventory_code, title
	FROM book_copy
	JOIN book USING(book)

SELECT title, first_name, last_name
	FROM book
	JOIN author_book USING(book_id)
	JOIN author USING(author_id)
	WHERE book_id = 34
	
SELECT *
	FROM book_copy
	where book_id = 33
SELECT title, inventory_code
	FROM book_copy bc
	JOIN book b USING(book_id)
	WHERE bc.inventory_code = '81797941';
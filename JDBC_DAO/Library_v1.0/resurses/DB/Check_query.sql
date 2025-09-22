
SELECT *
	FROM book
SELECT genre_id, book_id, title,genre_name
	FROM genre_book
	JOIN book USING(book_id)
	JOIN genre USING(genre_id)
	WHERE book_id = 31
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
	WHERE book_id = 31
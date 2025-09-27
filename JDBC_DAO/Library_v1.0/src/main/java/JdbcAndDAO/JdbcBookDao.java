package JdbcAndDAO;

import InterfaceDAO.BookDao;
import RowMappers.BookRowMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import ClassesDOJO.Book;
import Exception.DaoException;

public class JdbcBookDao implements BookDao {
    private final JdbcTemplate jdbcTemplate;
    private final BookRowMapper mapper = new BookRowMapper();
    public JdbcBookDao(DataSource dataSource){
        jdbcTemplate= new JdbcTemplate(dataSource);
    }

    //************************ Methods
    @Override
    public Book getBookById(int id){
        String query = """
                SELECT *
                FROM book
                WHERE book_id=?""";
        try{
            List<Book> books = jdbcTemplate.query(query,mapper,id);
            return books.isEmpty() ? null: books.get(0);
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }
    @Override
    public List<Book> getAllBooks() {
        List<Book> listBook = new ArrayList<>();
        String query = """
                SELECT *
                FROM book;""";
        try {
            return  jdbcTemplate.query(query, mapper);
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }
    @Override
    public List<Book> getBookByTile(String title, boolean isFull) {
        title=isFull?title : "%"+title+"%";
        String query= """
                SELECT *
                FROM book
                WHERE title ILIKE ?""";
        try {
            List<Book> authors= jdbcTemplate.query(query,mapper,title);
            return authors.isEmpty()? null: authors;
        }catch (EmptyResultDataAccessException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        } catch (NullPointerException e){
            throw new DaoException("Null ", e);
        }

    }
    @Override
    public List<Book> getBookByAuthorFullName(String firstName, String lastName) {
        firstName = "%"+firstName+"%";
        lastName = "%"+lastName+"%";
        String query= """
                SELECT b.book_id, b.title, b.publish_date, b.count_stock
                	FROM book b
                	JOIN author_book USING(book_id)
                	JOIN author USING(author_id)
                	WHERE author_id IN (
                		SELECT author_id
                		FROM author a
                		WHERE a.first_name ILIKE ? AND a.last_name ILIKE ?);""";
        try {
            return jdbcTemplate.query(query,mapper,firstName,lastName);
        }catch (EmptyResultDataAccessException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        } catch (NullPointerException e){
            throw new DaoException("Null ", e);
        }
    }
    @Override
    public Book createBook(Book blank) {
        String query = """
                INSERT INTO book(title, publish_date, count_stock)
                VALUES (?,?,?)
                RETURNING book_id;""";
        try {
            int id = jdbcTemplate.queryForObject(query, int.class, blank.getTitle(), blank.getPublishDate(), blank.getCountStock());
            return getBookById(id);
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }
    @Override
    public void createBookCopy(Book blankBook) {
        String query = """
                INSERT INTO book_copy (book_id, inventory_code)
                SELECT ?, random_inventory_number(8) FROM book WHERE title=?;""";
        try{
            for(int i = 1; i<=blankBook.getCountStock(); i++){
                jdbcTemplate.update(query,blankBook.getId(),blankBook.getTitle());
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }
    @Override
    public void updateBook(Book blankBook) {
    String query = """
           UPDATE book
           SET title=?, publish_date=?, count_stock=?
           WHERE book_id = ?""";
    try {
        int row = jdbcTemplate.update(query,blankBook.getTitle(),blankBook.getPublishDate(),blankBook.getCountStock(),blankBook.getId());
        if(row==0){
            throw new DaoException("Zero rows affected, expected at least one");
        }
    }catch (CannotGetJdbcConnectionException e) {
        throw new DaoException("Unable to connect to server or database", e);
    } catch (DataIntegrityViolationException e) {
        throw new DaoException("Data integrity violation", e);
    }
    }
    @Override
    public void deleteBook(Book bookForDelete) {
        long id = bookForDelete.getId();
        String deleteAB = """
                DELETE FROM author_book
                WHERE book_id = ?""";
        String deleteCB = """
                DELETE FROM book_copy
                WHERE book_id = ?""";
        String deleteGB = """
                DELETE FROM genre_book
                WHERE book_id = ?""";
        String deleteLoan= """
                DELETE FROM loan
                WHERE copy_id IN(
                SELECT copy_id
                FROM book_copy
                WHERE book_id=?)""";
        String deleteBook = """
                DELETE FROM book
                WHERE book_id=?""";
        try {
            jdbcTemplate.update(deleteAB,id);
            jdbcTemplate.update(deleteLoan,id);
            jdbcTemplate.update(deleteCB,id);
            jdbcTemplate.update(deleteGB,id);
            jdbcTemplate.update(deleteBook,id);
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

}

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
            return jdbcTemplate.queryForObject(query,mapper,id);
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
    public List<Book> getBookByTile(String title) {
        if (title.isEmpty()){
            return null;
        }
        title= "%"+title+"%";
        String query= """
                SELECT *
                FROM book
                WHERE title ILIKE ?""";
        try {
            return jdbcTemplate.query(query,mapper,title);
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
    public void createBookCopy(int valueCopy, long bookId) {
        String query = """
                INSERT INTO book_copy(book_id, inventory_code)
                VALUES (?,?)""";
        try{
            for(int i = 1; i<=valueCopy; i++){
                jdbcTemplate.update(query,bookId,"");
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }

    }

}

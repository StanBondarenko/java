package JdbcAndDAO;

import ClassesDOJO.Author;
import ClassesDOJO.Genre;
import InterfaceDAO.AuthorDao;
import RowMappers.AuthorRowMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import Exception.DaoException;

import java.util.List;
import java.util.Objects;

public class JdbcAuthorDao implements AuthorDao {
    private final JdbcTemplate jdbcTemplate;
    private final AuthorRowMapper mapper = new AuthorRowMapper();
    public JdbcAuthorDao(DataSource dataSource){
        jdbcTemplate= new JdbcTemplate(dataSource);
    }

    @Override
    public Author getAuthorById(int id) {
        String query= """
                SELECT *
                FROM author
                WHERE author_id=?""";
        try {
            List<Author> authors= jdbcTemplate.query(query,mapper,id);
            return authors.isEmpty() ? null: authors.getFirst();
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }
    @Override
    public List<Author> getAuthorByFirstName(String firstName, boolean isFull) {
        firstName=  isFull ? firstName : "%"+firstName+"%";
        String query = """
                SELECT *
                FROM author
                WHERE first_name ILIKE ?""";
        try {
            List<Author> authors= jdbcTemplate.query(query,mapper,firstName);
            return authors.isEmpty() ? null: authors;
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }
    @Override
    public List<Author> getAuthorByLastName(String lastName, boolean isFull) {
        lastName=  isFull ? lastName : "%"+lastName+"%";
        String query = """
                SELECT *
                FROM author
                WHERE last_name ILIKE  ?""";
        try {
            List<Author> authors= jdbcTemplate.query(query,mapper,lastName);
            return authors.isEmpty() ? null: authors;
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }
    @Override
    public List<Author> getAuthorByBookTitle(String title, boolean isFull) {
        title=isFull?title:"%"+title+"%";
        String query = """
                SELECT author_id, first_name, last_name, birthday, death_day
                	FROM author
                	JOIN author_book USING(author_id)
                	JOIN book USING(book_id)
                	WHERE title ILIKE ?""";
        try {
            List<Author> authors=jdbcTemplate.query(query,mapper,title);
            return authors.isEmpty() ? null: authors;
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }

    @Override
    public void deleteAuthor(Author author) {
        String deleteBA = """
                DELETE FROM author_book
                WHERE author_id = ?""";
        String deleteAuthor= """
                DELETE FROM author
                WHERE author_id = ?""";
        try {
            jdbcTemplate.update(deleteBA,author.getId());
            jdbcTemplate.update(deleteAuthor,author.getId());
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }

    @Override
    public void updateAuthor(Author author) {
        String query= """
                UPDATE author
                SET first_name=?, last_name=?, birthday=?, death_day=?
                WHERE author_id=?""";
        try {
            int row = jdbcTemplate.update(query,author.getAuthorFirstName(),author.getAuthorLastName(),author.getBirthday(),author.getDeathDate(),author.getId());
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
    public List<Author> getAllAuthors() {
        String query = """
                SELECT *
                FROM author""";
        try {
         return jdbcTemplate.query(query,mapper);
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }
    @Override
    public Author createAuthor(Author blank) {
        String query = """
                INSERT INTO author(first_name, last_name, birthday, death_day)
                VALUES (?,?,?,?)
                RETURNING author_id""";
        try {
            int id = jdbcTemplate.queryForObject(query,int.class,blank.getAuthorFirstName(),blank.getAuthorLastName(), blank.getBirthday(),blank.getDeathDate());
            return getAuthorById(id);
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }
    @Override
    public void addNewDataToAuthorBook(long authorId, long bookId) {
        String query = """
                INSERT INTO author_book(author_id, book_id)
                VALUES(?,?)""";
        try {
            jdbcTemplate.update(query,authorId,bookId);
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }





}

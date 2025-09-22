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
            return jdbcTemplate.queryForObject(query,mapper,id);
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
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

package JdbcAndDAO;

import ClassesDOJO.Genre;
import InterfaceDAO.GenreDao;
import RowMappers.GenreRowMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import Exception.DaoException;

import javax.sql.DataSource;
import java.util.List;

public class JdbcGenreDao implements GenreDao {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper mapper = new GenreRowMapper();
    public JdbcGenreDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Genre> getAllGenre() {
        String query = """
                SELECT *
                FROM genre""";
        try{
            return jdbcTemplate.query(query,mapper);
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }

    @Override
    public void addGenreToGenreBook(long bookId, long genreId) {
        String query = """
                INSERT INTO genre_book(genre_id, book_id)
                VALUES(?,?)""";
        try {
            jdbcTemplate.update(query,genreId,bookId);
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }catch (DataIntegrityViolationException e) {
            throw new DaoException("Data Integrity Violation", e);
        }
    }


}

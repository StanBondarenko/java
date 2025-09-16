package JDBCandDAO;

import RowMappers.BookRowMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import ClassesDOJO.Book;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import Exception.DaoException;

public class JdbcBookDao {
    private final JdbcTemplate jdbcTemplate;
    private final BookRowMapper mapper = new BookRowMapper();
    public JdbcBookDao(DataSource dataSource){
        jdbcTemplate= new JdbcTemplate(dataSource);
    }

    public List<Book> allBooks() {
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

}

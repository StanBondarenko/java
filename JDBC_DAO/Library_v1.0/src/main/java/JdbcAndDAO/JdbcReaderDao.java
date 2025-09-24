package JdbcAndDAO;

import ClassesDOJO.Reader;
import InterfaceDAO.ReaderDao;
import RowMappers.ReaderRowMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import Exception.DaoException;

import java.util.List;

public class JdbcReaderDao implements ReaderDao {
    private final JdbcTemplate jdbcTemplate;
    private final ReaderRowMapper mapper = new ReaderRowMapper();
    public JdbcReaderDao(DataSource dataSource){
        jdbcTemplate= new JdbcTemplate(dataSource);
    }

    @Override
    public Reader getReaderById(int id) {
        String query = """
                SELECT *
                FROM reader
                WHERE reader_id = ?""";
        try {
            List<Reader> readers = jdbcTemplate.query(query,mapper,id);
            return readers.isEmpty()? null: readers.getFirst();
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public Reader createNewReader(Reader reader) {
       String query= """
               INSERT INTO reader (first_name, last_name,address, phone_number, e_mail)
               VALUES(?,?,?,?,?)
               RETURNING reader_id;""";
       try {
           int id= jdbcTemplate.queryForObject(query, int.class,reader.getReaderFirstName(),
                   reader.getReaderLastName(),reader.getAddress(),reader.getPhoneNumber(),reader.geteMail());
           return getReaderById(id);
       }catch (CannotGetJdbcConnectionException e) {
           throw new DaoException("Unable to connect to server or database", e);
       } catch (DataIntegrityViolationException e) {
           throw new DaoException("Data integrity violation", e);
       }
    }
}


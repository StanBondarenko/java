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
    @Override
    public void updateReader(Reader reader) {
        String query = """
                UPDATE reader
                SET first_name=?, last_name=?, address=?,phone_number=?,e_mail=?
                WHERE reader_id=?;""";
        try {
            int numberOfRow =  jdbcTemplate.update(query,reader.getReaderFirstName(),reader.getReaderLastName(),
                    reader.getAddress(),reader.getPhoneNumber(),reader.geteMail(),reader.getId());
            if (numberOfRow==0){
                throw new DaoException("Zero rows affected, expected at least one");
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }
    @Override
    public List<Reader> getAllReaders() {
        String query= """
                SELECT *
                FROM reader;""";
        try {
            return jdbcTemplate.query(query,mapper);
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }
    @Override
    public void deleteReader(Reader readerForDelete) {
        String deleteLoan= """
                DELETE FROM loan
                WHERE reader_id=?;""";
        String deleteReader = """
                DELETE FROM reader
                WHERE reader_id=?""";
        try {
            jdbcTemplate.update(deleteLoan, readerForDelete.getId());
            jdbcTemplate.update(deleteReader,readerForDelete.getId());
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

}


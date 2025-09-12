package RowMappers;

import ClassesDOJO.Reader;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReaderRowMapper implements RowMapper {
    @Override
    public Reader mapRow(ResultSet rs, int rowNum) throws SQLException {
    Reader reader = new Reader();
    reader.setId(rs.getLong("reader_id"));
    reader.setReaderFirstName(rs.getString("first_name"));
    reader.setReaderLastName(rs.getString("last_name"));
    reader.setAddress(rs.getString("address"));
    reader.setPhoneNumber(rs.getString("phone_number"));
    reader.seteMail(rs.getString("e_mail"));
    return reader;
    }
}

package RowMappers;

import ClassesDOJO.Author;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AuthorRowMapper implements RowMapper<Author>{
    @Override
   public Author mapRow(ResultSet rs, int rowNum) throws SQLException{
        Author author = new Author();
        author.setId(rs.getLong("author_id"));
        author.setAuthorFirstName(rs.getString("first_name"));
        author.setAuthorLastName(rs.getString("last_name"));
        author.setBirthday(rs.getDate("birthday").toLocalDate());
        LocalDate deathDay = (rs.getDate("death_day")==null ? null : rs.getDate("death_day").toLocalDate());
        author.setDeathDate(deathDay);
        return author;
    }
}

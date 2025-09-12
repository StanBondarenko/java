package RowMappers;

import ClassesDOJO.Author;

import org.springframework.jdbc.core.RowMapper;
import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AuthorRowMapper implements RowMapper<Author>{
    @Override
   public Author mapRow(ResultSet rs, int rowNum) throws SQLException{
        Author author = new Author();
        return author;
    }
}

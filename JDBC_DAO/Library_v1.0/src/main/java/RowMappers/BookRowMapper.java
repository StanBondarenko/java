package RowMappers;

import ClassesDOJO.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper {
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("author_id"));
        book.setTitle(rs.getString("title"));
        book.setPublishDate(rs.getDate("publish_date").toLocalDate());
        book.setCountStock(rs.getInt("count_stock"));
        return book;
    }
}

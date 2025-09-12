package JDBCandDAO;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcGenreDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcGenreDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}

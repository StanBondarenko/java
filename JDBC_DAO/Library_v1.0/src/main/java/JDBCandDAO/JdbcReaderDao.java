package JDBCandDAO;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcReaderDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcReaderDao(DataSource dataSource){
        jdbcTemplate= new JdbcTemplate(dataSource);
    }
}


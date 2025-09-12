package JDBCandDAO;

import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

public class JdbcBookDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcBookDao(DataSource dataSource){
        jdbcTemplate= new JdbcTemplate(dataSource);
    }
}

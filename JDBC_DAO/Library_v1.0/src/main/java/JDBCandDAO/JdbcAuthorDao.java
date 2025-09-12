package JDBCandDAO;

import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

public class JdbcAuthorDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcAuthorDao(DataSource dataSource){
        jdbcTemplate= new JdbcTemplate(dataSource);
    }
}

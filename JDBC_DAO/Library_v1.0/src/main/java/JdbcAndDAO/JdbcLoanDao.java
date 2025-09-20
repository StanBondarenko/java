package JdbcAndDAO;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcLoanDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcLoanDao(DataSource dataSouce) {
        jdbcTemplate = new JdbcTemplate(dataSouce);
    }
}

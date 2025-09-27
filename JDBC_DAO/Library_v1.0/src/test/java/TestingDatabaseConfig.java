import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

@Configuration
public class TestingDatabaseConfig {

    private static final String DB_HOST = Objects.requireNonNullElse(System.getenv("DB_HOST"), "localhost");
    private static final String DB_PORT = Objects.requireNonNullElse(System.getenv("DB_PORT"), "5432");
    private static final String DB_NAME = Objects.requireNonNullElse(System.getenv("DB_NAME"), "libraryTest");

    private static final String DB_USERNAME = Objects.requireNonNullElse(System.getenv("DB_USERNAME"), "postgres");
    private static final String DB_PASSWORD = Objects.requireNonNullElse(System.getenv("DB_PASSWORD"), "111");

    private static final String ADMIN_DB = "postgres";
    private static final String ADMIN_USER = Objects.requireNonNullElse(System.getenv("ADMIN_DB_USERNAME"), "postgres");
    private static final String ADMIN_PASS = Objects.requireNonNullElse(System.getenv("ADMIN_DB_PASSWORD"), "111");

    private SingleConnectionDataSource adminDataSourceRef;
    private JdbcTemplate adminJdbcTemplateRef;
    private SingleConnectionDataSource testDataSourceRef;

    @Bean(name = "adminDataSource")
    public DataSource adminDataSource() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ds.setUrl("jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + ADMIN_DB);
        ds.setUsername(ADMIN_USER);
        ds.setPassword(ADMIN_PASS);
        ds.setSuppressClose(true);
        this.adminDataSourceRef = ds;
        return ds;
    }

    @Bean(name = "adminJdbcTemplate")
    public JdbcTemplate adminJdbcTemplate(@Qualifier("adminDataSource") DataSource adminDs) {
        JdbcTemplate jt = new JdbcTemplate(adminDs);
        this.adminJdbcTemplateRef = jt;
        return jt;
    }

    @Bean(name = "resetTestDatabase")
    public Object resetTestDatabase(@Qualifier("adminJdbcTemplate") JdbcTemplate adminJdbcTemplate) {
        try {
            adminJdbcTemplate.execute("DROP DATABASE IF EXISTS \"" + DB_NAME + "\" WITH (FORCE);");
        } catch (BadSqlGrammarException e) {
            adminJdbcTemplate.update("""
                SELECT pg_terminate_backend(pid)
                FROM pg_stat_activity
                WHERE datname = ? AND pid <> pg_backend_pid()
            """, DB_NAME);
            adminJdbcTemplate.execute("DROP DATABASE IF EXISTS \"" + DB_NAME + "\";");
        }
        adminJdbcTemplate.execute("CREATE DATABASE \"" + DB_NAME + "\" ENCODING 'UTF8' TEMPLATE template0;");
        return new Object();
    }

    @Bean(name = "testDataSource")
    @Primary
    @DependsOn("resetTestDatabase")
    public DataSource dataSource() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ds.setUrl("jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME);
        ds.setUsername(DB_USERNAME);
        ds.setPassword(DB_PASSWORD);
        ds.setAutoCommit(false);
        ds.setSuppressClose(true);
        this.testDataSourceRef = ds;
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("testDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean(name = "initSchema")
    @DependsOn({"resetTestDatabase", "testDataSource"})
    public Object initSchema(@Qualifier("testDataSource") DataSource dataSource) throws SQLException {
        ClassPathResource schema = new ClassPathResource("libraryTest.sql");
        try (Connection c = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(c, schema);
            c.commit();
        }
        return new Object();
    }

    @PostConstruct
    public void afterStart() {
    }

    @PreDestroy
    public void cleanup() {
        try { if (testDataSourceRef != null) testDataSourceRef.destroy(); } catch (Exception ignored) {}

        try {
            if (adminJdbcTemplateRef != null) {
                try {
                    adminJdbcTemplateRef.execute("DROP DATABASE IF EXISTS \"" + DB_NAME + "\" WITH (FORCE);");
                } catch (BadSqlGrammarException e) {
                    adminJdbcTemplateRef.update("""
                        SELECT pg_terminate_backend(pid)
                        FROM pg_stat_activity
                        WHERE datname = ? AND pid <> pg_backend_pid()
                    """, DB_NAME);
                    adminJdbcTemplateRef.execute("DROP DATABASE IF EXISTS \"" + DB_NAME + "\";");
                }
            }
        } catch (Exception ignored) {}

        try { if (adminDataSourceRef != null) adminDataSourceRef.destroy(); } catch (Exception ignored) {}
    }
}
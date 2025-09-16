package System;

import org.apache.commons.dbcp2.BasicDataSource;

public class Connector {
   private final BasicDataSource dataSource = new BasicDataSource();
    public Connector(){}

    public BasicDataSource getDataSource() {
        dataSource.setUrl("jdbc:postgresql://localhost:5432/library");
        dataSource.setUsername("postgres");
        dataSource.setPassword("111");
        return dataSource;
    }
}

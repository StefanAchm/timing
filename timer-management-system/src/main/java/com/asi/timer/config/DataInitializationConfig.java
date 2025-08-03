package com.asi.timer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataInitializationConfig {

    private final DataSource dataSource;

    public DataInitializationConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public void initDatabase() {

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

//        populator.addScript(new ClassPathResource("data-competitors.sql"));
//        populator.addScript(new ClassPathResource("data-rounds.sql"));

        populator.addScript(new ClassPathResource("users.sql"));

        populator.execute(dataSource);

    }

}

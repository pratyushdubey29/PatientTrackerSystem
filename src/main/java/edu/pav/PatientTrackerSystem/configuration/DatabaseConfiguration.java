package edu.pav.PatientTrackerSystem.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Data
@Configuration
@PropertySource(value = "classpath:application.properties")
public class DatabaseConfiguration {

    private @Value("${spring.datasource.url}") String url;
    private @Value("${spring.datasource.username}") String userName;
    private @Value("${spring.datasource.password}") String password;
    private @Value("${spring.jpa.datasource-platform}") String dialect;
    private @Value("${spring.jpa.show-sql}") String showSql;
    private @Value("${spring.jpa.hibernate.ddl-auto}") String ddlAuto;
}

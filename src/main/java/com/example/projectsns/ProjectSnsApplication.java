package com.example.projectsns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableJpaAuditing
public class ProjectSnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectSnsApplication.class, args);
    }

}

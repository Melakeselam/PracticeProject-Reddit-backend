package com.example.redditclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@ConfigurationPropertiesScan("com.example.redditclone.config")
@EnableJpaRepositories(basePackages = "com.example.redditclone.repository")
@SpringBootApplication
public class RedditcloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedditcloneApplication.class, args);
    }

}

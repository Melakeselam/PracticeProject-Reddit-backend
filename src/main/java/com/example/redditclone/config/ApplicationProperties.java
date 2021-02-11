package com.example.redditclone.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.application")
public class ApplicationProperties {
    private String name;
    private String authPath;
    private String url;
    private String accountVerificationUrl;
}

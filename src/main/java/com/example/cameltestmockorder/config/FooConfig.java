package com.example.cameltestmockorder.config;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "route.foo")
@Builder
public class FooConfig {
    private String fromRoute;
    private String toRoute;
}

package com.example.cameltestmockorder.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "my")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FooConfig {
    private String fromRoute;
    private String toRoute;
}

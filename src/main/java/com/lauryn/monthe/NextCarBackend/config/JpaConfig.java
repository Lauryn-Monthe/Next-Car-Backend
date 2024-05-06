package com.lauryn.monthe.NextCarBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@Configuration
@EnableJdbcAuditing(auditorAwareRef = "jpaAuditorAware")
public class JpaConfig {

    @Bean
    public AuditorAware<String> jpaAuditorAware() {
        return new AuditorAwareService();
    }

}

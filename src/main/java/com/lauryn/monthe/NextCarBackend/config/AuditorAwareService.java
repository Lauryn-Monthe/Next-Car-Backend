package com.lauryn.monthe.NextCarBackend.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditorAwareService implements AuditorAware<String>{

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.empty();
    }

}

package com.dhandev.dewallet;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class CustomAuditAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable("Ramdhan").filter(s -> !s.isEmpty());
    }
}

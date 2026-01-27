package com.example.gatewayservice.util;

import java.time.LocalDateTime;

public class EmailAuthCode {

    private final String code;
    private final LocalDateTime expiresAt;

    public EmailAuthCode(String code, LocalDateTime expiresAt) {
        this.code = code;
        this.expiresAt = expiresAt;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}

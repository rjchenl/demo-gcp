package com.example.userservice.dto;

import java.time.LocalDateTime;

public record UserWithTaskCountDto(Long id, String name, String email, LocalDateTime createdAt, long taskCount) {
}

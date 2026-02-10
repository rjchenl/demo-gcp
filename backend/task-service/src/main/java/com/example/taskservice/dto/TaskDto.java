package com.example.taskservice.dto;

import java.time.LocalDateTime;

public record TaskDto(Long id, String title, String description, boolean completed, Long userId, LocalDateTime createdAt) {
}

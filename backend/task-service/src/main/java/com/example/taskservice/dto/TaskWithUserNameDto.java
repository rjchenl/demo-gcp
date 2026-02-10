package com.example.taskservice.dto;

import java.time.LocalDateTime;

public record TaskWithUserNameDto(Long id, String title, String description, boolean completed,
                                  Long userId, String userName, LocalDateTime createdAt) {
}

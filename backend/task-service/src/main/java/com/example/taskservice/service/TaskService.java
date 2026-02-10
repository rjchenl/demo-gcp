package com.example.taskservice.service;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.dto.TaskWithUserNameDto;
import com.example.taskservice.dto.UserDto;
import com.example.taskservice.entity.Task;
import com.example.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final RestClient userServiceClient;

    public TaskService(TaskRepository taskRepository, RestClient userServiceClient) {
        this.taskRepository = taskRepository;
        this.userServiceClient = userServiceClient;
    }

    public List<TaskDto> findAll() {
        return taskRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public List<TaskDto> findByUserId(Long userId) {
        return taskRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    public TaskWithUserNameDto findByIdWithUserName(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));

        String userName = "Unknown";
        try {
            UserDto user = userServiceClient.get()
                    .uri("/internal/users/{id}", task.getUserId())
                    .retrieve()
                    .body(UserDto.class);
            if (user != null) {
                userName = user.name();
            }
        } catch (Exception e) {
            // User Service 不可用時，顯示 Unknown
        }

        return new TaskWithUserNameDto(
                task.getId(), task.getTitle(), task.getDescription(),
                task.isCompleted(), task.getUserId(), userName, task.getCreatedAt());
    }

    public TaskDto create(TaskDto dto) {
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setCompleted(dto.completed());
        task.setUserId(dto.userId());
        return toDto(taskRepository.save(task));
    }

    public TaskDto update(Long id, TaskDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setCompleted(dto.completed());
        return toDto(taskRepository.save(task));
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public long countByUserId(Long userId) {
        return taskRepository.countByUserId(userId);
    }

    private TaskDto toDto(Task task) {
        return new TaskDto(task.getId(), task.getTitle(), task.getDescription(),
                task.isCompleted(), task.getUserId(), task.getCreatedAt());
    }
}

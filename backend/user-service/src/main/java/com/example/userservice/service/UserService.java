package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserWithTaskCountDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RestClient taskServiceClient;

    public UserService(UserRepository userRepository, RestClient taskServiceClient) {
        this.userRepository = userRepository;
        this.taskServiceClient = taskServiceClient;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    public UserWithTaskCountDto findByIdWithTaskCount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        Long taskCount = taskServiceClient.get()
                .uri("/internal/tasks/count?userId={userId}", id)
                .retrieve()
                .body(Long.class);

        return new UserWithTaskCountDto(
                user.getId(), user.getName(), user.getEmail(),
                user.getCreatedAt(), taskCount != null ? taskCount : 0L);
    }

    public UserDto create(UserDto dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        return toDto(userRepository.save(user));
    }

    public UserDto update(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setName(dto.name());
        user.setEmail(dto.email());
        return toDto(userRepository.save(user));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }
}

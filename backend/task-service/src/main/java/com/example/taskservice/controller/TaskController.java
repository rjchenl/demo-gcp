package com.example.taskservice.controller;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.dto.TaskWithUserNameDto;
import com.example.taskservice.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDto> findAll(@RequestParam(required = false) Long userId) {
        if (userId != null) {
            return taskService.findByUserId(userId);
        }
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public TaskWithUserNameDto findById(@PathVariable Long id) {
        return taskService.findByIdWithUserName(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@RequestBody TaskDto dto) {
        return taskService.create(dto);
    }

    @PutMapping("/{id}")
    public TaskDto update(@PathVariable Long id, @RequestBody TaskDto dto) {
        return taskService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}

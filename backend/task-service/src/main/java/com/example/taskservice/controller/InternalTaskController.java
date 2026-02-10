package com.example.taskservice.controller;

import com.example.taskservice.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/tasks")
public class InternalTaskController {

    private final TaskService taskService;

    public InternalTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/count")
    public long countByUserId(@RequestParam Long userId) {
        return taskService.countByUserId(userId);
    }
}

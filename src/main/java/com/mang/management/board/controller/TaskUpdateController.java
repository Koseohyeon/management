package com.mang.management.board.controller;

import com.mang.management.board.dto.TaskDto;
import com.mang.management.board.entity.Task;
import com.mang.management.board.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TaskUpdateController {
    private final TaskService taskService;


    @MessageMapping("/update-task")
    @SendTo("/topic/tasks")
    public TaskDto updateTask(TaskDto taskDto) {
        return taskService.updateTask(taskDto); // DB 반영 후 결과 반환
    }
}

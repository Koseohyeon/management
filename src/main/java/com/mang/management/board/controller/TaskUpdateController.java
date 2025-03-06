package com.mang.management.board.controller;

import com.mang.management.board.entity.Task;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TaskUpdateController {

    @MessageMapping("/update-task")
    @SendTo("/topic/tasks")
    public Task updateTask(Task task){
    return task;
    }
}

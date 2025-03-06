package com.mang.management.board.controller;

import com.mang.management.board.entity.TaskList;
import com.mang.management.board.repository.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class TaskListController {

    @Autowired
    private TaskListRepository taskListRepository;

    @GetMapping
    public List<TaskList> getAllLists(){
        return taskListRepository.findAll();
    }

    @PostMapping
    public TaskList createList(@RequestBody TaskList taskList){
        return taskListRepository.save(taskList);
    }

    @DeleteMapping("/{id}")
    public void deleteList(@PathVariable Long id){
        taskListRepository.deleteById(id);
    }
}

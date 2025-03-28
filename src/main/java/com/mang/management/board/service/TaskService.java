package com.mang.management.board.service;

import com.mang.management.board.dto.TaskDto;
import com.mang.management.board.entity.Board;
import com.mang.management.board.entity.Task;
import com.mang.management.board.entity.TaskList;
import com.mang.management.board.repository.BoardRepository;
import com.mang.management.board.repository.TaskListRepository;
import com.mang.management.board.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final BoardRepository boardRepository;

    public TaskService(TaskRepository taskRepository, TaskListRepository taskListRepository, BoardRepository boardRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional
    public TaskDto updateTask(TaskDto dto) {
        // 기존 Task 조회
        Task task = taskRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Task 속성 수정
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.isCompleted());

        // taskList가 변경된 경우
        if (dto.getTaskListId() != null && !dto.getTaskListId().equals(task.getTaskList().getId())) {
            TaskList newTaskList = taskListRepository.findById(dto.getTaskListId())
                    .orElseThrow(() -> new RuntimeException("TaskList not found"));
            task.setTaskList(newTaskList);
        }

        // board 변경을 반영할 필요는 없지만, taskList에서 board를 설정해야 한다면
        if (dto.getBoardId() != null) {
            Board board = boardRepository.findById(dto.getBoardId())
                    .orElseThrow(() -> new RuntimeException("Board not found"));
            TaskList taskList = task.getTaskList();
            taskList.setBoard(board);  // taskList에 board 설정
        }

        // 수정된 Task 저장
        return TaskDto.from(taskRepository.save(task));
    }
}
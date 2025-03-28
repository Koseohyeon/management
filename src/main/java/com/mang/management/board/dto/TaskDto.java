package com.mang.management.board.dto;

import com.mang.management.board.entity.Task;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private Long taskListId;  // TaskList와의 관계를 위한 ID
    private Long boardId;     // Board와의 관계를 위한 ID

    // 엔티티 -> DTO 변환
    public static TaskDto from(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .taskListId(task.getTaskList().getId()) // taskListId 설정
                .boardId(task.getTaskList().getBoard().getId()) // boardId 설정
                .build();
    }
}
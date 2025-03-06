package com.mang.management.board.repository;

import com.mang.management.board.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskListRepository extends JpaRepository<TaskList,Long> {
}

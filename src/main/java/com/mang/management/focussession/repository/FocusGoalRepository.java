package com.mang.management.focussession.repository;

import com.mang.management.focussession.entity.FocusGoal;
import com.mang.management.focussession.entity.FocusSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface FocusGoalRepository extends JpaRepository<FocusGoal, Long>, FocusGoalCustomRepository {
    Optional<FocusGoal> findByUserIdAndGoalDate(Long userId, LocalDate goalDate);
}

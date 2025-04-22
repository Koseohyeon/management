package com.mang.management.focussession.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class FocusGoalCustomRepositoryImpl implements FocusGoalCustomRepository {

    private final EntityManager em;

    @Transactional
    @Override
    public void upsertGoal(Long userId, LocalDate goalDate, int minutes) {
        em.createNativeQuery(
                        "INSERT INTO focus_goal (user_id, goal_date, goal_minutes) " +
                                "VALUES (:userId, :goalDate, :goalMinutes) " +
                                "ON DUPLICATE KEY UPDATE goal_minutes = :goalMinutes"
                )
                .setParameter("userId", userId)
                .setParameter("goalDate", goalDate)
                .setParameter("goalMinutes", minutes)
                .executeUpdate();
    }
}

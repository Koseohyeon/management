package com.mang.management.focussession.repository;

import java.time.LocalDate;
public interface FocusGoalCustomRepository {
    void upsertGoal(Long userId, LocalDate goalDate, int minutes);
}
package com.mang.management.focussession.repository;

import com.mang.management.focussession.entity.FocusSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FocusSessionRepository extends JpaRepository<FocusSession, Long> {
    List<FocusSession> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT FUNCTION('DATE', fs.startTime), SUM(fs.totalMinutes) " +
            "FROM FocusSession fs " +
            "WHERE fs.user.id = :userId " +
            "GROUP BY FUNCTION('DATE', fs.startTime)")
    List<Object[]> findDailyTotalByUser(@Param("userId") Long userId);

    @Query("SELECT FUNCTION('DATE', fs.startTime), SUM(fs.totalMinutes) as total " +
            "FROM FocusSession fs " +
            "WHERE fs.user.id = :userId " +
            "GROUP BY FUNCTION('DATE', fs.startTime) " +
            "ORDER BY total DESC")
    List<Object[]> findLongestFocusDay(@Param("userId") Long userId);

    @Query("SELECT FUNCTION('DATE', fs.startTime), SUM(fs.totalMinutes) as total " +
            "FROM FocusSession fs " +
            "WHERE fs.user.id = :userId " +
            "GROUP BY FUNCTION('DATE', fs.startTime) " +
            "ORDER BY total ASC")
    List<Object[]> findShortestFocusDay(@Param("userId") Long userId);
}
package com.mang.management.focussession.service;

import com.mang.management.focussession.dto.DailyFocusStat;
import com.mang.management.focussession.dto.FocusSessionRequest;
import com.mang.management.focussession.entity.FocusGoal;
import com.mang.management.focussession.entity.FocusSession;
import com.mang.management.focussession.repository.FocusGoalRepository;
import com.mang.management.focussession.repository.FocusSessionRepository;
import com.mang.management.member.entity.User;
import com.mang.management.member.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class FocusService {

    private final FocusSessionRepository focusSessionRepository;
    private final UserRepository userRepository;
    private final FocusGoalRepository focusGoalRepository;

    private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

    @Transactional
    public void saveFocusSession(Long userId, FocusSessionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new IllegalArgumentException("시작 시간과 종료 시간은 필수입니다.");
        }

        FocusSession session = FocusSession.builder()
                .user(user)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .memo(request.getMemo())
                .build();

        focusSessionRepository.save(session);
    }

    public List<DailyFocusStat> getDailyStats(Long userId, LocalDate startDate, LocalDate endDate) {
        // KST 기준 하루 시작과 끝 계산
        ZonedDateTime zonedStart = startDate.atStartOfDay(SEOUL_ZONE);
        ZonedDateTime zonedEnd = endDate.plusDays(1).atStartOfDay(SEOUL_ZONE).minusNanos(1);

        LocalDateTime start = zonedStart.toLocalDateTime();
        LocalDateTime end = zonedEnd.toLocalDateTime();

        List<Object[]> result = focusSessionRepository.findDailyTotalByUserBetween(userId, start, end);

        return result.stream()
                .map(r -> new DailyFocusStat(((java.sql.Date) r[0]).toLocalDate(), (Long) r[1]))
                .toList();
    }

    public DailyFocusStat getLongestFocusDay(Long userId) {
        List<Object[]> result = focusSessionRepository.findLongestFocusDay(userId);
        if (!result.isEmpty()) {
            LocalDate date = ((java.sql.Date) result.get(0)[0]).toLocalDate();
            Long total = (Long) result.get(0)[1];
            return new DailyFocusStat(date, total);
        }
        return null;
    }

    public DailyFocusStat getShortestFocusDay(Long userId) {
        List<Object[]> result = focusSessionRepository.findShortestFocusDay(userId);
        if (!result.isEmpty()) {
            LocalDate date = ((java.sql.Date) result.get(0)[0]).toLocalDate();
            Long total = (Long) result.get(0)[1];
            return new DailyFocusStat(date, total);
        }
        return null;
    }

    public void setDailyGoal(Long userId, int minutes) {
        focusGoalRepository.upsertGoal(userId, LocalDate.now(SEOUL_ZONE), minutes);
    }

    public int getTodayGoal(Long userId) {
        return focusGoalRepository.findByUserIdAndGoalDate(userId, LocalDate.now(SEOUL_ZONE))
                .map(FocusGoal::getGoalMinutes)
                .orElse(0);
    }
}
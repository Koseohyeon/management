package com.mang.management.focussession.service;

import com.mang.management.focussession.dto.DailyFocusStat;
import com.mang.management.focussession.dto.FocusSessionRequest;
import com.mang.management.focussession.entity.FocusSession;
import com.mang.management.focussession.repository.FocusSessionRepository;
import com.mang.management.member.entity.User;
import com.mang.management.member.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class FocusService {

    private final FocusSessionRepository focusSessionRepository;
    private final UserRepository userRepository;

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
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();

        List<Object[]> result = focusSessionRepository.findDailyTotalByUser(userId);

        return result.stream()
                .map(r -> new DailyFocusStat(((java.sql.Date) r[0]).toLocalDate(), (Long) r[1]))
                .filter(stat -> !stat.getDate().isBefore(startDate) && !stat.getDate().isAfter(endDate))
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
}
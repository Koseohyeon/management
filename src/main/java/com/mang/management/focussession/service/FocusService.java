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
        // 이제 totalMinutes는 @PrePersist에서 자동 계산됨
       // long totalMinutes = Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();

        FocusSession session = FocusSession.builder()
                .user(user)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
               // .totalMinutes(totalMinutes)
                .memo(request.getMemo())
                .build();

       // session.calculateTotalMinutes(); // ✅ 자동 계산 추가


        focusSessionRepository.save(session);
    }
    public List<DailyFocusStat> getDailyStats(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();
        List<FocusSession> sessions = focusSessionRepository.findByUserIdAndStartTimeBetween(userId, start, end);

        Map<LocalDate, Long> dailyTotals = new TreeMap<>();
        for (FocusSession session : sessions) {
            LocalDate date = session.getStartTime().toLocalDate();
            dailyTotals.put(date, dailyTotals.getOrDefault(date, 0L) + session.getTotalMinutes());
        }

        return dailyTotals.entrySet().stream()
                .map(e -> new DailyFocusStat(e.getKey(), e.getValue()))
                .toList();
    }

    public DailyFocusStat getLongestFocusDay(Long userId) {
        List<Object[]> result = focusSessionRepository.findLongestFocusDay(userId);
        if (!result.isEmpty()) {
            LocalDate date = ((LocalDateTime) result.get(0)[0]).toLocalDate();
            Long total = (Long) result.get(0)[1];
            return new DailyFocusStat(date, total);
        }
        return null;
    }
}

package com.mang.management.focussession.controller;

import com.mang.management.focussession.dto.FocusSessionRequest;
import com.mang.management.focussession.service.FocusService;
import com.mang.management.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/focus")
@RequiredArgsConstructor
public class FocusController {

    private final FocusService focusService;

    @PostMapping("/save")
    public void saveFocusSession(@RequestBody FocusSessionRequest request,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        focusService.saveFocusSession(userId, request);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam String startDate,
                                      @RequestParam String endDate,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return ResponseEntity.ok(focusService.getDailyStats(userDetails.getId(), start, end));
    }

    @GetMapping("/longest")
    public ResponseEntity<?> getLongest(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(focusService.getLongestFocusDay(userDetails.getId()));
    }
}


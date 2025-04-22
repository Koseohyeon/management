package com.mang.management.focussession.controller;

import com.mang.management.focussession.service.FocusService;
import com.mang.management.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/focus")
@RequiredArgsConstructor
public class FocusGoalController {

    private final FocusService focusService;

    @PostMapping("/goal")
    public ResponseEntity<?> setDailyGoal(@RequestParam int minutes,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        focusService.setDailyGoal(userDetails.getId(), minutes);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/goal")
    public ResponseEntity<?> getTodayGoal(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(focusService.getTodayGoal(userDetails.getId()));
    }
}

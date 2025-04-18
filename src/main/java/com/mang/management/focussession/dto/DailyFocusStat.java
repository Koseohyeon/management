package com.mang.management.focussession.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class DailyFocusStat {
    //통계 응답 DTO
    private LocalDate date;
    private Long totalMinutes;

    public DailyFocusStat(LocalDate date, Long totalMinutes) {
        this.date = date;
        this.totalMinutes = totalMinutes;
    }
}
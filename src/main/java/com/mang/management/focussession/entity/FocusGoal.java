package com.mang.management.focussession.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "focus_goal", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "goalDate"}))
public class FocusGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDate goalDate;

    private int goalMinutes;
}
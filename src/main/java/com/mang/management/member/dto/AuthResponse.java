package com.mang.management.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    //로그인 응답 dto
    private String token;

}

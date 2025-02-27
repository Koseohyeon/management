package com.mang.management.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    //로그인 요청 dto
    private String email;
    private String password;
}

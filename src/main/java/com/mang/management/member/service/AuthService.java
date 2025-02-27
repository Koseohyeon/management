package com.mang.management.member.service;

import com.mang.management.jwt.JwtUtil;
import com.mang.management.member.dto.AuthRequest;
import com.mang.management.member.dto.AuthResponse;
import com.mang.management.member.entity.User;
import com.mang.management.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(AuthRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("이미 존재하는 이메일 입니다");
        }

        User user=new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode((request.getPassword())));


        userRepository.save(user);
    }

    public AuthResponse login(AuthRequest request){
        User user=userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("잘못된 이메일 또는 비밀번호입니다"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword() )){
            throw new RuntimeException(" passwordEncoder 잘못된 이메일 또는 미밀번호입니다");
        }
        String token=jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}

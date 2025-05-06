package com.kdw.sns.auth.controller;

import com.kdw.sns.auth.dto.SignupDto;
import com.kdw.sns.member.entity.Member;
import com.kdw.sns.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * ✅ 회원가입 API
     * - 이메일 중복 확인
     * - 비밀번호 암호화 후 저장
     */
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignupDto signupDto) {

        // 이메일 중복 체크
        if (memberRepository.existsByEmail(signupDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
        }

        // 회원 저장
        Member member = Member.createMember(signupDto, passwordEncoder);

        memberRepository.save(member);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }
}

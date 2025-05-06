package com.kdw.sns.auth.controller;

import com.kdw.sns.auth.dto.request.OAuthSignupDto;
import com.kdw.sns.auth.dto.request.SignupDto;
import com.kdw.sns.member.entity.Member;
import com.kdw.sns.member.entity.type.Role;
import com.kdw.sns.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> register(@RequestBody @Valid SignupDto signupDto) {

        // 이메일 중복 체크
        if (memberRepository.existsByEmail(signupDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다.");
        }

        // 회원 저장
        Member member = Member.createMember(signupDto, passwordEncoder);

        memberRepository.save(member);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    @PostMapping("/oauth/signup")
    public ResponseEntity<?> oauthSignup(@ModelAttribute @Valid OAuthSignupDto dto, BindingResult bindingResult, Model model) {


        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("입력값 오류가 있습니다.");
        }

        if (memberRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일입니다.");
        }

        Member member = Member.createMemberFromOAuth(dto);
        memberRepository.save(member);

        return ResponseEntity.status(HttpStatus.CREATED).body("소셜 회원가입이 완료되었습니다 ✅");
    }
}

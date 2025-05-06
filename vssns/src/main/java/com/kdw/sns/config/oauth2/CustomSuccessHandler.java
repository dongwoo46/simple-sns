package com.kdw.sns.config.oauth2;


import java.io.IOException;
import java.util.*;

import com.kdw.sns.auth.dto.response.CustomOAuth2User;
import com.kdw.sns.config.security.JwtTokenProvider;
import com.kdw.sns.member.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.kdw.sns.member.repository.MemberRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String email = customOAuth2User.getEmail();
        String provider = customOAuth2User.getProvider();
        String providerId = customOAuth2User.getProviderId();

        Optional<Member> memberOpt = memberRepository.findByEmail(email);

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();

            String role = customOAuth2User.getAuthorities().stream()
                    .findFirst()
                    .orElseThrow()
                    .getAuthority();

            String token = jwtTokenProvider.generateToken(
                    "access",
                    member.getMemberId(),
                    email,
                    member.getMembername(),
                    member.getNickname(),
                    role
            );

            response.addCookie(createCookie("access_token", token));
            response.sendRedirect("http://localhost:8080/"); // ✅ 메인 페이지 등
        } else {
            // 아직 회원가입 안 된 사용자 → 추가정보 입력 화면으로 이동
            String redirectUrl = String.format(
                    "http://localhost:8080/oauth/signup?email=%s&provider=%s&providerId=%s",
                    email, provider, providerId
            );
            response.sendRedirect(redirectUrl);
        }
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60); // 1시간
        cookie.setPath("/");
        return cookie;
    }
}
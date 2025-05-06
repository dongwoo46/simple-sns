package com.kdw.sns.config.security;


import com.kdw.sns.auth.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtLogoutFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1️⃣ 로그아웃 경로 + POST 메서드 확인
        if (!request.getRequestURI().equals("/auth/logout") || !request.getMethod().equalsIgnoreCase("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2️⃣ 쿠키에서 refresh 토큰 꺼내기
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 3️⃣ 만료 여부 확인
        try {
            jwtTokenProvider.validateToken(refresh);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 4️⃣ 토큰 category 검사 (access면 안 됨)
        String category = jwtTokenProvider.getCategoryFromToken(refresh);
        if (!"refresh".equals(category)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 5️⃣ DB에서 존재 여부 확인
        boolean exists = refreshTokenRepository.existsByRefresh(refresh);
        if (!exists) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 6️⃣ DB에서 Refresh 삭제
        refreshTokenRepository.deleteByRefresh(refresh);

        // 7️⃣ 클라이언트 쿠키 삭제
        Cookie expiredCookie = new Cookie("refresh", null);
        expiredCookie.setMaxAge(0);
        expiredCookie.setPath("/");
        response.addCookie(expiredCookie);

        // 8️⃣ 성공 응답
        response.setStatus(HttpServletResponse.SC_OK);
    }
}


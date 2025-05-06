package com.kdw.sns.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdw.sns.auth.entity.RefreshToken;
import com.kdw.sns.auth.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider; // JWT 유틸 (email, role, userId 등 포함)
    private final RefreshTokenRepository refreshTokenRepository;

    private static final long ACCESS_TOKEN_EXPIRE_MS = 10 * 60 * 1000L; // 10분
    private static final long REFRESH_TOKEN_EXPIRE_MS = 24 * 60 * 60 * 1000L; // 24시간

    public JwtLoginFilter(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;

        // ✅ 여기! 로그인 요청을 가로챌 경로 설정
        this.setFilterProcessesUrl("/auth/login");
    }

    /**
     * 로그인 요청 시 호출됨. 유저네임/비밀번호로 인증 시도
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            // JSON 형식 요청 파싱 (email + password)
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);

            String email = credentials.get("email");
            String password = credentials.get("password");

            if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
                throw new AuthenticationServiceException("Email or Password not provided");
            }

            // email 기반 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, password);

            // AuthenticationManager 통해 인증 수행
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new AuthenticationServiceException("Request parsing failed", e);
        }
    }

    /**
     * 로그인 성공 시 호출됨 - AccessToken + RefreshToken 발급
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {

        // 👇 인증된 사용자 정보 가져오기 (principal = CustomUserDetails)
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getMemberId();
        String email = userDetails.getEmail();
        String username = userDetails.getUsername();
        String role = userDetails.getRole();
        String nickname = userDetails.getNickname();

        // JWT AccessToken & RefreshToken 생성
        String accessToken = jwtTokenProvider.generateToken("access",userId, email, username, nickname, role);
        String refreshToken = jwtTokenProvider.generateToken("refresh",userId, email, username, nickname, role); // 👈 refresh도 같은 정보로 생성

        // RefreshToken 저장 (DB or Redis 등)
        saveRefreshToken(userId, email, refreshToken, REFRESH_TOKEN_EXPIRE_MS);

        // 응답에 토큰 세팅
        setSuccessResponse(response, accessToken, refreshToken);
    }

    /**
     * 로그인 실패 시 401 반환
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"Invalid username or password\"}");
    }

    /**
     * 사용자의 권한 추출 (현재는 사용 안 함 — CustomUserDetails에서 직접 꺼냄)
     */
    private String extractUserRole(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new IllegalStateException("User has no roles"));
    }

    /**
     * RefreshToken을 DB에 저장
     */
    private void saveRefreshToken(Long userId, String email, String refreshToken, long expiredMs) {
        RefreshToken refreshEntity = RefreshToken.builder()
                .userId(userId)
                .email(email)
                .refresh(refreshToken)
                .expiration(new Date(System.currentTimeMillis() + expiredMs).toInstant().toString())
                .build();

        refreshTokenRepository.save(refreshEntity);
    }

    /**
     * 로그인 성공 응답 - 헤더와 쿠키에 토큰 전달
     */
    private void setSuccessResponse(HttpServletResponse response,
                                    String accessToken,
                                    String refreshToken) throws IOException {
        // accessToken은 Authorization 헤더에
        response.setHeader("Authorization", "Bearer " + accessToken);

        // refreshToken은 HttpOnly 쿠키로 저장
        response.addCookie(createHttpOnlyCookie("refresh", refreshToken));

        // ✅ JSON 형태로 access/refresh token 응답 바디에 포함
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"accessToken\":\"" + accessToken + "\", \"refreshToken\":\"" + refreshToken + "\"}");
    }

    /**
     * HttpOnly 쿠키 생성 (XSS 방지용)
     */
    private Cookie createHttpOnlyCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (REFRESH_TOKEN_EXPIRE_MS / 1000)); // 초 단위
        cookie.setPath("/"); // 전체 경로 적용
        return cookie;
    }
}


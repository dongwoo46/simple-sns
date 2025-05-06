package com.kdw.sns.config.security;


import com.kdw.sns.auth.repository.RefreshTokenRepository;
import com.kdw.sns.config.oauth2.CustomOAuth2UserService;
import com.kdw.sns.config.oauth2.CustomSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenProvider jwtTokenProvider; // ✅ JWT 토큰 생성 및 검증 유틸
    private final RefreshTokenRepository refreshTokenRepository; // ✅ RefreshToken 저장소 (JPA or Redis)

    /**
     * 비밀번호 암호화를 위한 BCryptPasswordEncoder Bean 등록
     * - 회원가입 및 로그인 시 비밀번호 암호화/검증에 사용
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager Bean 등록
     * - JwtLoginFilter에서 사용자 인증을 위해 필요
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Spring Security의 필터 체인 및 보안 설정 정의
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // ✅ CORS 설정 - React 등 외부 프론트엔드에서 API 요청 허용
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOriginPatterns(List.of("*"));// ✅ 더 유연하게 허용
            config.setAllowedMethods(Collections.singletonList("*")); // 모든 HTTP 메서드 허용
            config.setAllowedHeaders(Collections.singletonList("*")); // 모든 요청 헤더 허용
            config.setAllowCredentials(true); // 쿠키 포함 요청 허용
            config.setExposedHeaders(Collections.singletonList("Authorization")); // 응답 헤더에 Authorization 포함 허용
            config.setMaxAge(3600L);
            return config;
        }));

        // ✅ 기본 시큐리티 설정 해제 (JWT 기반이므로 불필요)
        http.csrf(csrf -> csrf.disable());
        http.formLogin(form -> form.disable());
        http.httpBasic(basic -> basic.disable());

        //oauth2
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth/login") // 👈 사용자가 직접 가는 로그인 페이지
                .successHandler(customSuccessHandler)
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                )
                .redirectionEndpoint(redir -> redir
                        .baseUri("/auth/login/oauth2/code/*") // 👈 리디렉션 받을 URI
                )
        );

        // ✅ 인가 정책 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/signup", "/oauth/**", "/auth/**").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
        );

        // ✅ 세션 미사용 (JWT 기반이므로 상태 유지 불필요)
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 1️⃣ 로그인 요청 처리 - /login 요청 시 아이디/비번 검증 후 Access/Refresh Token 발급
        http.addFilterAt(
                new JwtLoginFilter(
                        authenticationManager(authenticationConfiguration),
                        jwtTokenProvider,
                        refreshTokenRepository
                ),
                UsernamePasswordAuthenticationFilter.class
        );

        // 2️⃣ 로그인 이후 JWT 검증 필터 - 모든 요청에 대해 Authorization 헤더의 AccessToken 유효성 검사
        http.addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider),
                JwtLoginFilter.class
        );

        // 3️⃣ 로그아웃 요청 처리 필터 - RefreshToken 제거 및 쿠키 삭제
        http.addFilterBefore(
                new JwtLogoutFilter(jwtTokenProvider, refreshTokenRepository),
                LogoutFilter.class
        );

        return http.build();
    }
}
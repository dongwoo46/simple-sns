package com.kdw.sns.auth.dto.response;

import com.kdw.sns.auth.dto.request.OAuthLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final OAuthLoginDto oAuthLoginDto;

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", oAuthLoginDto.getEmail());
        attributes.put("provider", oAuthLoginDto.getProvider());
        attributes.put("providerId", oAuthLoginDto.getProviderId());
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_USER");
    }

    @Override
    public String getName() {
        return oAuthLoginDto.getProvider() + "_" + oAuthLoginDto.getProviderId();
    }

    // 🔧 잘못된 참조 수정
    public String getProvider() {
        return oAuthLoginDto.getProvider();
    }

    public String getProviderId() {
        return oAuthLoginDto.getProviderId();
    }

    public String getEmail() {
        return oAuthLoginDto.getEmail();
    }
}


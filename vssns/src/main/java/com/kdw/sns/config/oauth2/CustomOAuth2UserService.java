package com.kdw.sns.config.oauth2;

import com.kdw.sns.auth.dto.request.OAuthLoginDto;
import com.kdw.sns.auth.dto.response.CustomOAuth2User;
import com.kdw.sns.auth.dto.response.ResGoogleDto;
import com.kdw.sns.auth.dto.response.ResNaverDto;
import com.kdw.sns.auth.dto.response.ResOAuth2;
import com.kdw.sns.member.entity.Member;
import com.kdw.sns.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // ex: "google", "naver"
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        ResOAuth2 oAuth2Response = switch (registrationId) {
            case "naver" -> new ResNaverDto(oAuth2User.getAttributes());
            case "google" -> new ResGoogleDto(oAuth2User.getAttributes());
            default -> throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        };

        // 기존 회원 여부 확인
        Optional<Member> optionalMember = memberRepository.findByEmail(oAuth2Response.getEmail());

        if (optionalMember.isEmpty()) {
            // 저장은 하지 않음
            OAuthLoginDto dto = OAuthLoginDto.builder()
                    .email(oAuth2Response.getEmail())
                    .provider(oAuth2Response.getProvider())
                    .providerId(oAuth2Response.getProviderId())
                    .build();


            return new CustomOAuth2User(dto);

        } else {
            // 기존 회원 정보 업데이트
            Member existingMember = optionalMember.get();

            OAuthLoginDto dto = OAuthLoginDto.builder()
                    .email(existingMember.getEmail())
                    .provider(oAuth2Response.getProvider())
                    .providerId(oAuth2Response.getProviderId())
                    .build();

            return new CustomOAuth2User(dto);
        }
    }
}


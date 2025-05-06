package com.kdw.sns.auth.dto.response;

import java.util.Map;

public class ResNaverDto implements ResOAuth2 {

    private final Map<String, Object> attribute;

    public ResNaverDto(Map<String, Object> attribute) {
        // "response" 키 안에 실제 사용자 정보가 들어있음
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        Object email = attribute.get("email");
        return (email != null) ? email.toString() : "no-email@naver.com";
    }

    @Override
    public String getName() {

        return attribute.get("name").toString();
    }

    public String getPhone() {
        Object mobile = attribute.get("mobile"); // 또는 "mobile_e164" 사용 가능
        return (mobile != null) ? mobile.toString() : "no-phone";
    }
}

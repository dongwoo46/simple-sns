package com.kdw.sns.auth.dto.response;

import java.util.Map;

public class ResGoogleDto implements ResOAuth2 {

    private final Map<String, Object> attribute;

    public ResGoogleDto(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString(); // 구글 고유 ID
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    @Override
    public String getPhone() {
        return null;
    }
}
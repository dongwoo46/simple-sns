package com.kdw.sns.auth.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthLoginDto {
    private String email;
    private String provider;
    private String providerId;
}

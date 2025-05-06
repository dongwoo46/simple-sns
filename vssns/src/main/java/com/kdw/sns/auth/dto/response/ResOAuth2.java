package com.kdw.sns.auth.dto.response;

public interface ResOAuth2 {
    //제공자 (Ex. naver, google, ...)
    String getProvider();
    //제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    //이메일
    String getEmail();
    //사용자 실명 (설정한 이름)
    String getName();

    String getPhone();       // 전화번호 반환이 목적이면 확실히 명시

}

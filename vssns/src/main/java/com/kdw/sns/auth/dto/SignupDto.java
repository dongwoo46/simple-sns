package com.kdw.sns.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    @Size(max = 50, message = "이메일은 50자 이내여야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상 100자 이내여야 합니다.")
    private String password;

    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Size(max = 50, message = "사용자 이름은 50자 이내여야 합니다.")
    private String username;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 100, message = "닉네임은 100자 이내여야 합니다.")
    private String nickname;
}
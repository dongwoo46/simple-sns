package com.kdw.sns.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Size(max = 50)
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 100)
    private String nickname;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "\\d{10,20}", message = "전화번호는 숫자 10~20자여야 합니다.")
    private String phoneNumber;
}

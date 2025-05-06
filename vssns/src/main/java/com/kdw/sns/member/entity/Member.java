package com.kdw.sns.member.entity;

import com.kdw.sns.auth.dto.request.OAuthSignupDto;
import com.kdw.sns.auth.dto.request.SignupDto;
import com.kdw.sns.common.entity.BaseEntity;
import com.kdw.sns.member.entity.type.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, length = 50, unique = true)
    private String membername;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 100, unique = true)
    private String nickname;

    @Column(nullable = false, length = 1)
    private int status = 1; // 예: 1 = 활성, 2 = 비활성, 0 = 삭제

    @Column(length = 255)
    private String profileImage;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Lob
    @Column(nullable = true)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /**
     * 🔒 Member 객체 생성을 Builder로만 제한하고,
     *     외부에서는 정적 팩토리 메서드를 통해서만 사용하게 하기 위함.
     */
    @Builder(access = AccessLevel.PRIVATE)
    private Member(String membername, String email, String password,
                   String nickname, int status, String profileImage,
                   String bio, String phoneNumber, Role role) {
        this.membername = membername;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        this.profileImage = profileImage;
        this.bio = bio;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // 🔐 정적 팩토리: 일반 회원가입
    public static Member createMember(SignupDto dto, BCryptPasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(dto.getEmail())
                .membername(dto.getUsername())
                .nickname(dto.getNickname())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .status(1)
                .profileImage(null)
                .bio(null)
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    //  정적 팩토리 메서드 - 소셜 회원가입
    public static Member createMemberFromOAuth(OAuthSignupDto dto) {
        return Member.builder()
                .membername(dto.getUsername())
                .email(dto.getEmail())
                .password("OAUTH") // 소셜 전용 더미
                .nickname(dto.getNickname())
                .phoneNumber(dto.getPhoneNumber())
                .role(Role.USER)
                .status(1)
                .profileImage(null)
                .bio(null)
                .build();
    }

}

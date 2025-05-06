package com.kdw.sns.member.entity;

import com.kdw.sns.auth.dto.SignupDto;
import com.kdw.sns.common.entity.BaseEntity;
import com.kdw.sns.member.entity.type.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Lob
    @Column(nullable = true)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Builder
    public Member(String membername, String email, String password, String nickname,
                  int status, String profileImage, String bio, Role role) {
        this.membername = membername;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        this.profileImage = profileImage;
        this.bio = bio;
        this.role = role;
    }

    public static Member createMember(SignupDto dto, BCryptPasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(dto.getEmail())
                .membername(dto.getUsername())
                .nickname(dto.getNickname())
                .password(passwordEncoder.encode(dto.getPassword())) // ✅ 여기서 암호화
                .role(Role.USER)
                .status(1)
                .profileImage(null)
                .bio(null)
                .build();
    }

}

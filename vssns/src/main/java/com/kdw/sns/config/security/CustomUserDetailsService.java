package com.kdw.sns.config.security;

import com.kdw.sns.member.entity.Member;
import com.kdw.sns.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 로그인시 username이 아닌 email로 로그인 할떄 필요
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("✅ 로그인 시도 email: " + email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("❌ 이메일 없음: " + email);
                    return new UsernameNotFoundException("회원이 존재하지 않습니다.");
                });

        return new CustomUserDetails(
                member.getMemberId(),
                member.getMembername(),
                member.getNickname(),
                member.getEmail(),
                member.getRole().name(),
                member.getPassword()
        );
    }
}

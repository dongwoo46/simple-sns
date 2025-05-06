package com.kdw.sns.member.repository;

import com.kdw.sns.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일로 사용자 존재 여부 확인
     * - 회원가입 시 중복 체크에 사용
     */
    boolean existsByEmail(String email);

    /**
     * 이메일로 사용자 조회
     * - 로그인 시 UserDetailsService에서 사용자 불러올 때 사용
     */
    Optional<Member> findByEmail(String email);
}

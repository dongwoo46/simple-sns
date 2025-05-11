package com.kdw.sns.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class MemberRestrictionId implements Serializable {

    @Column(name = "restricter_id", nullable = false)
    private Long restricterId; // 제재한 사람

    @Column(name = "restricted_member_id", nullable = false)
    private Long restrictedMemberId; // 제재 당한 사람
}

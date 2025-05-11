package com.kdw.sns.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_restriction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRestriction {

    @EmbeddedId
    private MemberRestrictionId id;

    @MapsId("restricterId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restricter_id", nullable = false)
    private Member restricter;

    @MapsId("restrictedMemberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restricted_member_id", nullable = false)
    private Member restrictedMember;

    @Column(name = "can_dm", nullable = false)
    private boolean canDM;

    @Column(name = "can_comment", nullable = false)
    private boolean canComment;

    @Column(name = "can_tag", nullable = false)
    private boolean canTag;

    @Column(name = "block", nullable = false)
    private boolean block;
}


package com.kdw.sns.feed.entity;

import com.kdw.sns.common.entity.BaseEntity;
import com.kdw.sns.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "feed_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedLike extends BaseEntity {

    @EmbeddedId
    private FeedLikeId id;

    @MapsId("likerId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liker_id", nullable = false)
    private Member liker;

    @MapsId("likedFeedId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liked_feed_id", nullable = false)
    private Feed likedFeed;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}


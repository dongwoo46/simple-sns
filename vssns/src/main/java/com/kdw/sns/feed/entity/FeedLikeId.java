package com.kdw.sns.feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FeedLikeId implements Serializable {

    @Column(name = "liker_id")
    private Long likerId;

    @Column(name = "liked_feed_id")
    private Long likedFeedId;
}


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
public class FeedTagId implements Serializable {

    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "feed_id")
    private Long feedId;
}


package com.kdw.sns.feed.entity;

import com.kdw.sns.common.entity.BaseEntity;
import com.kdw.sns.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "feed")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedid")
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Lob
    @Column(name = "images")
    private String images;  // JSON 문자열로 저장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_feed_id")
    private Feed sharedFeed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedTag> tags = new ArrayList<>();
}


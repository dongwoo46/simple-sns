package com.kdw.sns.feed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagid")
    private Long id;

    @Column(name = "tag_name", length = 100, nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedTag> feeds = new ArrayList<>();
}

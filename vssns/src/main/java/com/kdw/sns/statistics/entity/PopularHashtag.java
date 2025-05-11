package com.kdw.sns.statistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "popular_hashtags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopularHashtag {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "hashtag", length = 100, nullable = false)
    private String hashtag;

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "snapshot_time", nullable = false)
    private LocalDate snapshotTime;
}

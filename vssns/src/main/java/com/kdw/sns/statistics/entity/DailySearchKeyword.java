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
@Table(name = "daily_feed_stat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailySearchKeyword {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "count", nullable = false)
    private int count;

    @Column(name = "keyword", length = 100, nullable = false)
    private String keyword;
}
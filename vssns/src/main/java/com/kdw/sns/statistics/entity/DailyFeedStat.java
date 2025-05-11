package com.kdw.sns.statistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "daily_feed_stat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyFeedStat {

    @Id
    @Column(name = "feed_stat_id", nullable = false)
    private Long feedStatId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "total_count", nullable = false)
    private int totalCount;

    @Column(name = "created_feed_count", nullable = false)
    private int createdFeedCount;
}

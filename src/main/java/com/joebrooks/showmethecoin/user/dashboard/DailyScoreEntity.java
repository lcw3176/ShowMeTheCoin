package com.joebrooks.showmethecoin.user.dashboard;

import com.joebrooks.showmethecoin.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "daily_score")
public class DailyScoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "today_earn_price")
    private double todayEarnPrice;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user")
    private UserEntity user;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}

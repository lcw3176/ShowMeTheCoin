package com.joebrooks.showmethecoin.repository.dailyScore;

import com.joebrooks.showmethecoin.repository.BaseTimeEntity;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "daily_score")
public class DailyScoreEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "today_earn_price")
    private double todayEarnPrice;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity userId;
}

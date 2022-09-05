package com.joebrooks.showmethecoin.repository.dailyScore;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyScoreRepository extends JpaRepository<DailyScoreEntity, Long> {

    List<DailyScoreEntity> findAllByUser(UserEntity user);
}

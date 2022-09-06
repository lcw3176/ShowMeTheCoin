package com.joebrooks.showmethecoin.user.dashboard;

import com.joebrooks.showmethecoin.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyScoreRepository extends JpaRepository<DailyScoreEntity, Long> {

    List<DailyScoreEntity> findAllByUser(UserEntity user);
}

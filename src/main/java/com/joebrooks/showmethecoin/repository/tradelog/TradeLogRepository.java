package com.joebrooks.showmethecoin.repository.tradelog;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TradeLogRepository extends JpaRepository<TradeLogEntity, Long> {

    Page<TradeLogEntity> findByUser(UserEntity user, Pageable pageable);

    List<TradeLogEntity> findAllByUserAndOrderEndDateBetween(UserEntity user, LocalDateTime start, LocalDateTime end);
}

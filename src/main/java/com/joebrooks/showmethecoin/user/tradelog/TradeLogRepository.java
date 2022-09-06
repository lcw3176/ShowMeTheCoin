package com.joebrooks.showmethecoin.user.tradelog;

import com.joebrooks.showmethecoin.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TradeLogRepository extends JpaRepository<TradeLogEntity, Long> {

    Page<TradeLogEntity> findByUser(UserEntity user, Pageable pageable);

    List<TradeLogEntity> findByUser(UserEntity user, Sort sort);

    List<TradeLogEntity> findAllByUser(UserEntity user);

    List<TradeLogEntity> findAllByUserAndOrderEndDateBetween(UserEntity user, LocalDateTime start, LocalDateTime end);
}

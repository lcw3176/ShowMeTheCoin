package com.joebrooks.showmethecoin.repository.trade;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TradeRepository extends JpaRepository<TradeEntity, Long> {

    Page<TradeEntity> findByUserId(UserEntity user, Pageable pageable);

    List<TradeEntity> findAllByUserIdAndCreatedDateBetween(UserEntity user, LocalDateTime start, LocalDateTime end);
}

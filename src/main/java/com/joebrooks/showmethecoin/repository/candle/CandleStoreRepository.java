package com.joebrooks.showmethecoin.repository.candle;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandleStoreRepository extends JpaRepository<CandleStoreEntity, Long> {

    List<CandleStoreEntity> findAllByMarket(String market);

    List<CandleStoreEntity> findAllByMarket(String market, Sort sort);
}

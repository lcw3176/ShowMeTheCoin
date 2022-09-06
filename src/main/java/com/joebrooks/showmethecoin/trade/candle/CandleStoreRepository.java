package com.joebrooks.showmethecoin.trade.candle;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandleStoreRepository extends JpaRepository<CandleStoreEntity, Long> {


    long countAllByMarketAndCandleMinute(String market, CandleMinute candleMinute);

    List<CandleStoreEntity> findAllByMarketAndCandleMinute(String market, CandleMinute candleMinute, Sort sort);
}

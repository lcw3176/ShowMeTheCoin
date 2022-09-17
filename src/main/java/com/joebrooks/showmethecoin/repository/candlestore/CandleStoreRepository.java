package com.joebrooks.showmethecoin.repository.candlestore;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandleStoreRepository extends JpaRepository<CandleStoreEntity, Long> {


    Optional<CandleStoreEntity> findByDateKstAndMarket(String dateKst, String market);

    long countAllByMarketAndCandleMinute(String market, CandleMinute candleMinute);

    List<CandleStoreEntity> findAllByMarketAndCandleMinute(String market, CandleMinute candleMinute, Sort sort);

    List<CandleStoreEntity> findAllByMarketAndCandleMinuteAndDateKstBetween(String market,
                                                                            CandleMinute candleMinute,
                                                                            String startTime,
                                                                            String endTime,
                                                                            Sort sort);
}

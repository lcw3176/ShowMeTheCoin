package com.joebrooks.showmethecoin.repository.candlestore;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CandleStoreRepository extends JpaRepository<CandleStoreEntity, Long> {

    @Query("SELECT DISTINCT market FROM candle_store")
    List<String> findDistinctMarket();

    void deleteAllByMarket(String market);


}

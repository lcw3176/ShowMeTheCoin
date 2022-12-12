package com.joebrooks.showmethecoin.repository.pricestore;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PriceStoreRepository extends JpaRepository<PriceStoreEntity, Long> {

    @Query("SELECT DISTINCT market FROM price_store")
    List<String> findDistinctMarket();

    void deleteAllByMarket(String market);


}

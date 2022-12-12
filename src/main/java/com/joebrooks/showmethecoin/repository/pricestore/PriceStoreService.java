package com.joebrooks.showmethecoin.repository.pricestore;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PriceStoreService {

    private final PriceStoreRepository priceStoreRepository;

    public void deleteAll(){
        priceStoreRepository.deleteAll();
    }

    public void deleteAllByMarket(String market){
        priceStoreRepository.deleteAllByMarket(market);
    }
}

package com.joebrooks.showmethecoin.repository.candlestore;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CandleStoreService {

    private final CandleStoreRepository candleStoreRepository;

    @PostConstruct
    private void init(){
        candleStoreRepository.deleteAll();
    }

    public void deleteAll(){
        candleStoreRepository.deleteAll();
    }

    public void deleteAllByMarket(String market){
        candleStoreRepository.deleteAllByMarket(market);
    }
}

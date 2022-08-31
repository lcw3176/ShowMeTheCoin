package com.joebrooks.showmethecoin.repository.candle;

import com.joebrooks.showmethecoin.autotrade.upbit.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CandleStoreService {

    private final CandleStoreRepository candleStoreRepository;


    public List<CandleStoreEntity> getAll(CoinType coinType){
        return candleStoreRepository.findAllByMarket(coinType.getName());
    }

    public void save(CandleStoreEntity candleStoreEntity){
        candleStoreRepository.save(candleStoreEntity);
    }

    public List<CandleStoreEntity> getSortedCandleFromRecent(CoinType coinType){
        return candleStoreRepository.findAllByMarket(coinType.getName(), Sort.by(Sort.Direction.DESC, "dateKst"));
    }

    public void changeRecentCandle(CandleStoreEntity candleStoreEntity){
        List<CandleStoreEntity> lst = candleStoreRepository.findAllByMarket(
                candleStoreEntity.getMarket(), Sort.by(Sort.Direction.DESC, "dateKst"));

        if(!lst.isEmpty()){
            CandleStoreEntity temp = lst.get(0);
            candleStoreRepository.delete(temp);
            candleStoreRepository.save(candleStoreEntity);
        }

    }

    public void removeMostOlderCandle(String market){
        List<CandleStoreEntity> lst = candleStoreRepository.findAllByMarket(market, Sort.by(Sort.Direction.DESC, "dateKst"));

        if(!lst.isEmpty()){
            CandleStoreEntity temp = lst.get(lst.size() - 1);
            candleStoreRepository.delete(temp);
        }
    }

    public void deleteAll(){
        candleStoreRepository.deleteAll();
    }
}

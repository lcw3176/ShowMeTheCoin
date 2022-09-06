package com.joebrooks.showmethecoin.trade.candle;

import com.joebrooks.showmethecoin.trade.upbit.CoinType;
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


    public long getSize(CoinType coinType, CandleMinute candleMinute){
        return candleStoreRepository.countAllByMarketAndCandleMinute(coinType.getName(), candleMinute);
    }

    public void save(CandleStoreEntity candleStoreEntity){
        candleStoreRepository.save(candleStoreEntity);
    }

    public List<CandleStoreEntity> getSortedCandleFromRecent(CoinType coinType, CandleMinute minute){
        return candleStoreRepository.findAllByMarketAndCandleMinute(
                coinType.getName(), minute, Sort.by(Sort.Direction.DESC, "dateKst"));
    }

    public void changeRecentCandle(CandleStoreEntity candleStoreEntity){
        List<CandleStoreEntity> lst = candleStoreRepository.findAllByMarketAndCandleMinute(
                candleStoreEntity.getMarket(), candleStoreEntity.getCandleMinute(), Sort.by(Sort.Direction.DESC, "dateKst"));

        if(!lst.isEmpty()){
            CandleStoreEntity temp = lst.get(0);
            candleStoreRepository.delete(temp);
            candleStoreRepository.save(candleStoreEntity);
        }

    }

    public void removeMostOlderCandle(String market, CandleMinute candleMinute){
        List<CandleStoreEntity> lst = candleStoreRepository.findAllByMarketAndCandleMinute(
                market, candleMinute, Sort.by(Sort.Direction.DESC, "dateKst"));

        if(!lst.isEmpty()){
            CandleStoreEntity temp = lst.get(lst.size() - 1);
            candleStoreRepository.delete(temp);
        }
    }

    public List<CandleStoreEntity> getCandles(CoinType coinType, CandleMinute minute){
        return candleStoreRepository.findAllByMarketAndCandleMinute(
                coinType.getName(), minute, Sort.by(Sort.Direction.DESC, "dateKst"));
    }

    public void deleteAll(){
        candleStoreRepository.deleteAll();
    }
}

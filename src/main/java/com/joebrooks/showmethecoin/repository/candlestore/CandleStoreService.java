package com.joebrooks.showmethecoin.repository.candlestore;

import com.joebrooks.showmethecoin.trade.ICandleResponse;
import com.joebrooks.showmethecoin.trade.autotrade.TradingCoinList;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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


    public long getSize(CoinType coinType, CandleMinute candleMinute){
        return candleStoreRepository.countAllByMarketAndCandleMinute(coinType.getName(), candleMinute);
    }

    public boolean isExist(String dateKst, CoinType coinType, CandleMinute candleMinute){
        return candleStoreRepository.findByDateKstAndMarketAndCandleMinute(dateKst, coinType.getName(), candleMinute).isPresent();
    }

    public void save(CandleStoreEntity candleStoreEntity){
        candleStoreRepository.save(candleStoreEntity);
    }

    public void changeValue(int index, CoinType coinType, CandleMinute candleMinute, ICandleResponse candleResponse){
        List<CandleStoreEntity> candleStoreEntityList = candleStoreRepository.findAllByMarketAndCandleMinute(
                coinType.getName(), candleMinute, Sort.by(Sort.Direction.DESC, "dateKst"));

        candleStoreEntityList.get(index).changeHighPrice(candleResponse.getHighPrice());
        candleStoreEntityList.get(index).changeLowPrice(candleResponse.getLowPrice());
        candleStoreEntityList.get(index).changeTradePrice(candleResponse.getTradePrice());

        candleStoreEntityList.get(index).changeAccTradePrice(candleResponse.getAccTradePrice());
        candleStoreEntityList.get(index).changeAccTradeVolume(candleResponse.getAccTradeVolume());

        candleStoreEntityList.get(index).changeTimeStamp(candleResponse.getTimeStamp());

        if(TradingCoinList.WHITELIST.contains(coinType)){
            candleStoreRepository.save(candleStoreEntityList.get(index));
        }

    }

    public List<CandleStoreEntity> getSortedCandleFromRecent(CoinType coinType, CandleMinute minute){
        return candleStoreRepository.findAllByMarketAndCandleMinute(
                coinType.getName(), minute, Sort.by(Sort.Direction.DESC, "dateKst"));
    }

    public List<String> getCandlesDistinctByMarket(){
        return candleStoreRepository.findDistinctMarket();
    }


    public void removeMostOlderCandle(CoinType coinType, CandleMinute candleMinute){
        CandleStoreEntity temp = candleStoreRepository.findFirstByMarketAndCandleMinuteOrderByDateKstAsc(coinType.getName(), candleMinute);
        candleStoreRepository.delete(temp);
    }

    public List<CandleStoreEntity> getCandles(CoinType coinType, CandleMinute minute){
        return candleStoreRepository.findAllByMarketAndCandleMinute(
                coinType.getName(), minute, Sort.by(Sort.Direction.DESC, "dateKst"));
    }


    public List<CandleStoreEntity> getCandles(CoinType coinType, CandleMinute minute, String start, String end){
        return candleStoreRepository.findAllByMarketAndCandleMinuteAndDateKstBetween(
                coinType.getName(), minute, start, end, Sort.by(Sort.Direction.DESC, "dateKst"));
    }


    public void deleteAll(){
        candleStoreRepository.deleteAll();
    }

    public void deleteAllByMarket(String market){
        candleStoreRepository.deleteAllByMarket(market);
    }
}

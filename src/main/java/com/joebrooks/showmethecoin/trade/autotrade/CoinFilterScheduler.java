package com.joebrooks.showmethecoin.trade.autotrade;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreService;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoService;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigService;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import com.joebrooks.showmethecoin.trade.upbit.UpbitUtil;
import com.joebrooks.showmethecoin.trade.upbit.ticker.TickerResponse;
import com.joebrooks.showmethecoin.trade.upbit.ticker.TickerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoinFilterScheduler {

    private final TickerService tickerService;
    private final UserConfigService userConfigService;
    private final TradeInfoService tradeInfoService;
    private final CandleStoreService candleStoreService;
    private static final List<CoinType> whiteList = new LinkedList<>();
    private static final List<CoinType> blackList = new LinkedList<>();

    @PostConstruct
    private void init(){
        setBlackList();
        setWhiteList();
    }


    private void setBlackList(){
        blackList.add(CoinType.XRP);
        blackList.add(CoinType.SAND);
    }

    private void setWhiteList(){
        int delayMillis = 100;

        whiteList.clear();

        Map<Double, CoinType> tempMap = new TreeMap<>(Comparator.reverseOrder());

        for(CoinType coinType : CoinType.values()){
            TickerResponse response = tickerService.getTicker(coinType);
            tempMap.put(response.getAccTradePrice24h(), coinType);

            UpbitUtil.delay(delayMillis);
        }

        for(Map.Entry<Double, CoinType> coinTypeEntry : tempMap.entrySet()){
            if(!blackList.contains(coinTypeEntry.getValue()) && coinTypeEntry.getKey() / 1000000 >= 10000){
                whiteList.add(coinTypeEntry.getValue());
            }

        }


        // fixme 거래 했었던 코인 중 화이트리스트 제외되었을대 대책 세우기
        userConfigService.getAllUserConfig().forEach(user -> {
            tradeInfoService.getAllTradeCoins(user.getUser()).forEach(coin -> {
                if(!whiteList.contains(coin)){
                    whiteList.add(coin);
                }
            });
        });


        log.info("\n" +
                "\n거래 대상 코인 초기화" +
                "\n{}" +
                "\n", whiteList.toString());
    }

    @Scheduled(cron = "0 0 0/3 * * *", zone = "Asia/Seoul")
    public void resetWhiteList(){

        userConfigService.getAllUserConfig().forEach(user -> {
            user.stopTrading();
            userConfigService.save(user);
        });


        setWhiteList();
        candleStoreService.deleteAll();

        userConfigService.getAllUserConfig().forEach(user -> {
            user.startTrading();
            userConfigService.save(user);
        });
    }

    public List<CoinType> getTradingAllowedList(){
        return whiteList;
    }
}

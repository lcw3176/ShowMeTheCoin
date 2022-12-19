package com.joebrooks.showmethecoin.trade.autotrade;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.userconfig.UserConfigService;
import com.joebrooks.showmethecoin.trade.strategy.StrategyType;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import com.joebrooks.showmethecoin.trade.upbit.UpbitUtil;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoTradeExecutor {

    private final AutoTradeCore autoTradeCore;
    private final CandleService candleService;
    private final UserConfigService userConfigService;

    @Scheduled(fixedDelay = 1000)
    public void execute() {

        int candleLoadCount = 200;
        int candleDelayMillis = 100;

        List<StrategyType> strategyTypeList = userConfigService.getUsedStrategies();

        strategyTypeList.forEach(strategyType -> {

            for (CoinType coinType : TradingCoinList.WHITELIST) {

                List<CandleStoreEntity> candles = candleService.getCandles(coinType, strategyType.getCandleMinute(),
                        candleLoadCount);
                autoTradeCore.execute(candles, strategyType);

                UpbitUtil.delay(candleDelayMillis);
            }

        });

    }

}


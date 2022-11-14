package com.joebrooks.showmethecoin.trade.backtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.showmethecoin.global.util.TimeFormatter;
import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreService;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.upbit.UpbitUtil;
import com.joebrooks.showmethecoin.trade.upbit.candles.CandleService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
@RequiredArgsConstructor
@Slf4j
public class BackTestExecutor {

    private final BackTestCore backTestCore;
    private final CandleService candleService;
    private final CandleStoreService candleStoreService;
    private final ObjectMapper mapper;

    public void execute(BackTestRequest request, WebSocketSession session){

        try{
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(request.getStartDate().getTime());
            endDate.add(Calendar.MINUTE, request.getCandleMinute().getValue() * 200);

            Calendar candleLoadDate = Calendar.getInstance();
            candleLoadDate.setTime(endDate.getTime());
            List<TradeInfoEntity> tradeInfoLst = new ArrayList<>();

            BackTestInnerModel model = BackTestInnerModel
                    .builder()
                    .balance(0D)
                    .candleMinute(request.getCandleMinute())
                    .cashBalance(0D)
                    .coinBalance(0D)
                    .gain(0D)
                    .maxBetCount(request.getMaxBetCount())
                    .startBalance(request.getStartBalance())
                    .strategyType(request.getStrategyType())
                    .tradeCoin(request.getTradeCoin())
                    .build();

            while (true){
                int candleDelayMillis = 100;

                if(candleLoadDate.getTime().after(request.getEndDate().getTime())){
                    candleLoadDate.setTime(request.getEndDate().getTime());
                }

                candleService.getCandles(request.getTradeCoin(), TimeFormatter.convert(candleLoadDate.getTime()), request.getStrategyType().getCandleMinute());

                UpbitUtil.delay(1000);

                while(true){
                    List<CandleStoreEntity> candleStoreEntities
                            = candleStoreService.getCandles(
                            request.getTradeCoin(),
                            request.getCandleMinute(),
                            TimeFormatter.convert(request.getStartDate().getTime()),
                            TimeFormatter.convert(endDate.getTime()));


                    request.getStartDate().add(Calendar.MINUTE, request.getCandleMinute().getValue());
                    endDate.add(Calendar.MINUTE, request.getCandleMinute().getValue());

                    BackTestResponse response = backTestCore.execute(model, candleStoreEntities, request.getStrategyType(), tradeInfoLst);

                    if(session.isOpen()){
                        session.sendMessage(new TextMessage(mapper.writeValueAsString(response)));
                    }

                    if(endDate.getTime().equals(candleLoadDate.getTime())){
                        break;
                    }

                }

                if(candleLoadDate.getTime().equals(request.getEndDate().getTime())){
                    session.sendMessage(new TextMessage(mapper.writeValueAsString(BackTestResponse.builder()
                            .finish(true)
                            .gain(model.getGain() - request.getStartBalance())
                            .build())));

                    break;
                }

                UpbitUtil.delay(candleDelayMillis);
                candleLoadDate.add(Calendar.MINUTE, request.getCandleMinute().getValue() * 200);
            }

        } catch (Exception e){
            log.error(e.getMessage(), e);
        } finally {
            candleStoreService.deleteAll();
        }
    }

}


package com.joebrooks.showmethecoin.upbitTrade.auto;

import com.joebrooks.showmethecoin.global.graph.GraphStatus;
import com.joebrooks.showmethecoin.global.log.LoggingService;
import com.joebrooks.showmethecoin.global.routine.mail.ReportSender;
import com.joebrooks.showmethecoin.global.trade.TradeResult;
import com.joebrooks.showmethecoin.global.trade.TradeStatus;
import com.joebrooks.showmethecoin.repository.dailyScore.DailyScoreEntity;
import com.joebrooks.showmethecoin.repository.dailyScore.DailyScoreService;
import com.joebrooks.showmethecoin.repository.trade.TradeEntity;
import com.joebrooks.showmethecoin.repository.trade.TradeService;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.repository.user.UserService;
import com.joebrooks.showmethecoin.upbitTrade.account.AccountResponse;
import com.joebrooks.showmethecoin.upbitTrade.account.AccountService;
import com.joebrooks.showmethecoin.upbitTrade.candles.CandleResponse;
import com.joebrooks.showmethecoin.upbitTrade.candles.CandleService;
import com.joebrooks.showmethecoin.upbitTrade.indicator.Indicator;
import com.joebrooks.showmethecoin.upbitTrade.indicator.IndicatorService;
import com.joebrooks.showmethecoin.upbitTrade.indicator.type.IndicatorType;
import com.joebrooks.showmethecoin.upbitTrade.order.CheckOrderRequest;
import com.joebrooks.showmethecoin.upbitTrade.order.CheckOrderResponse;
import com.joebrooks.showmethecoin.upbitTrade.order.OrderRequest;
import com.joebrooks.showmethecoin.upbitTrade.order.OrderService;
import com.joebrooks.showmethecoin.upbitTrade.upbit.CoinType;
import com.joebrooks.showmethecoin.upbitTrade.upbit.OrderType;
import com.joebrooks.showmethecoin.upbitTrade.upbit.Side;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoRoutine {

    private AutoCommand autoCommand = AutoCommand.STOP;

    private final IndicatorService indicatorService;
    private final AccountService accountService;
    private final OrderService orderService;
    private final LoggingService loggingService;
    private final CandleService candleService;
    private final TradeService tradeService;
    private final UserService userService;
    private final DailyScoreService dailyScoreService;

    @Value("${auto.rsi.buy}")
    private int buy;

    @Value("${auto.rsi.sell}")
    private int sell;

    private final double initCashValue = 10000D;
    private double minCash = initCashValue;
    private double weight = 0.1D;
    private boolean isAvailable = true;

    private final double initValue = 100000000D;
    private double lastTradePrice = initValue;
    private CandleResponse lastTradeCandle = null;


    private final CoinType coinType = CoinType.WAVES;
    private final List<IndicatorType> indicator = new LinkedList<>();


    private UserEntity user;

    public void setCommand(AutoCommand command, UserEntity user){
        this.autoCommand = command;
        this.user = user;
    }



    @PostConstruct
    public void init(){
        indicator.add(IndicatorType.RSI);
        indicator.add(IndicatorType.Divergence);
    }


    @Scheduled(fixedDelay = 3000)
    public void mainAutomationRoutine(){

        try{
            if(autoCommand.equals(AutoCommand.STOP)){
                return;
            }

            if(!isAvailable){
                return;
            }

            List<CandleResponse> candles = candleService.getCandles(coinType);
            List<Indicator> indicatorList = indicatorService.execute(indicator, candles);

            Indicator rsi = indicatorList.stream()
                        .filter(data -> data.getType().equals(IndicatorType.RSI))
                        .findFirst()
                        .orElseThrow(() -> {
                            throw new RuntimeException(IndicatorType.RSI + " 보조지표가 없습니다");
                        });

            Indicator divergence = indicatorList.stream()
                    .filter(data -> data.getType().equals(IndicatorType.Divergence))
                    .findFirst()
                    .orElseThrow(() -> {
                        throw new RuntimeException(IndicatorType.Divergence + " 보조지표가 없습니다");
                    });

            CandleResponse nowCandle = candles.get(0);

            if(rsi.getValue() <= buy
                    && rsi.getStatus().equals(GraphStatus.STRONG_RISING)
                    && divergence.getStatus().equals(GraphStatus.FALLING)
                    && (lastTradeCandle == null || !nowCandle.getDateKst().equals(lastTradeCandle.getDateKst())) ){

                AccountResponse accountResponse = Arrays.stream(accountService.getAccountData())
                        .filter(data -> data.getCurrency().equals("KRW"))
                        .findFirst()
                        .orElseThrow(() ->{
                            throw  new RuntimeException("계좌 정보가 없습니다");
                        });

                double myBalance = Math.ceil(Double.parseDouble(accountResponse.getBalance())) ;

                if(myBalance > minCash && nowCandle.getTradePrice() < lastTradePrice){
                    double coinVolume = minCash / nowCandle.getTradePrice();

                    OrderRequest orderRequest = OrderRequest.builder()
                            .market(coinType.getName())
                            .side(Side.bid)
                            .price(Double.toString(nowCandle.getTradePrice()))
                            .volume(Double.toString(coinVolume))
                            .ordType(OrderType.limit)
                            .build();

                    orderService.requestOrder(orderRequest);

                    log.info("{}: 매수 {}", coinType.getKoreanName(), nowCandle.getTradePrice());

                    lastTradePrice = nowCandle.getTradePrice();
                    lastTradeCandle = nowCandle;

                    minCash = initCashValue + (weight * initCashValue);
                    weight += 0.1D;
                }

            }

            if(rsi.getValue() >= sell){

                AccountResponse coinResponse = Arrays.stream(accountService.getAccountData())
                        .filter(data -> data.getCurrency().equals(coinType.getName().split("-")[1]))
                        .findFirst()
                        .orElse(AccountResponse.builder().balance("0").build());

                double coinBalance = Double.parseDouble(coinResponse.getBalance());

                if(coinBalance > 0){
                    OrderRequest orderRequest = OrderRequest.builder()
                            .market(coinType.getName())
                            .side(Side.ask)
                            .price(Double.toString(nowCandle.getTradePrice()))
                            .volume(Double.toString(coinBalance))
                            .ordType(OrderType.limit)
                            .build();

                    orderService.requestOrder(orderRequest);

                    log.info("{}: 매도 {}", coinType.getKoreanName(), nowCandle.getTradePrice());
                    lastTradePrice = initValue;
                    minCash = initCashValue;
                    weight = 0.1D;
                }
            }

        } catch (ReadTimeoutException e){
            log.info("타임아웃");
        } catch (Exception e){
            loggingService.log(e);
            isAvailable = false;
        }
    }

    @Scheduled(cron = "0 30 10 * * *", zone = "Asia/Seoul")
    public void computeBalance(){

        if(autoCommand.equals(AutoCommand.STOP)){
            return;
        }


        List<TradeEntity> tradeEntities =
                tradeService.getTradeLogs(user,
                        LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(1),
                        LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        double buy = 0D;
        double sell = 0D;

        for (TradeEntity tradeEntity : tradeEntities) {
            buy += tradeEntity.getBuyPrice();
            sell += tradeEntity.getSellPrice();
        }

        dailyScoreService.addScore(DailyScoreEntity.builder()
                .todayEarnPrice(sell - buy)
                .userId(user)
                .build());

    }


    @Scheduled(cron = "0 */30 * * * *", zone = "Asia/Seoul")
    public void orderManager(){
        if(autoCommand.equals(AutoCommand.STOP)){
            return;
        }

        Page<TradeEntity> pages = tradeService.getTradeLogs(user, 0);
        List<CheckOrderResponse> responses = orderService.checkOrder(
                        CheckOrderRequest.builder()
                                .state(TradeStatus.DONE.toString().toLowerCase())
                                .build());

        for(int i = 0; i < responses.size(); i++){

            if(LocalDateTime.parse(responses.get(i).getCreatedAt().split("\\+")[0])
                    .isBefore(pages.getContent().get(0).getCreatedDate())){
                break;
            }

            if(responses.get(i).getSide().equals(Side.ask.toString())){

                double sell = 0D;
                double buy = 0D;

                for(int j = i; j < responses.size(); j++){
                    if(LocalDateTime.parse(responses.get(j).getCreatedAt().split("\\+")[0])
                            .isBefore(pages.getContent().get(0).getCreatedDate())
                            && j != i){
                        break;
                    }

                    if (responses.get(j).getSide().equals(Side.ask.toString())) {
                        sell += Double.parseDouble(responses.get(j).getPrice())
                                * Double.parseDouble(responses.get(j).getExecuteVolume());

                    } else {
                        buy += Double.parseDouble(responses.get(j).getPrice())
                                * Double.parseDouble(responses.get(j).getExecuteVolume());
                    }
                }

                TradeResult result;

                if(sell > buy){
                    result = TradeResult.Benefit;
                } else {
                    result = TradeResult.Loss;
                }

                tradeService.addTradeLog(TradeEntity.builder()
                        .userId(user)
                        .tradeResult(result)
                        .sellPrice(sell)
                        .buyPrice(buy)
                        .status(TradeStatus.DONE)
                        .coinType(coinType)
                        .build());

                AccountResponse accountResponse = Arrays.stream(accountService.getAccountData())
                        .filter(data -> data.getCurrency().equals("KRW"))
                        .findFirst()
                        .orElseThrow(() ->{
                            throw  new RuntimeException("계좌 정보가 없습니다");
                        });

                user.changeBalance(Double.parseDouble(accountResponse.getBalance()));
                userService.save(user);
                break;
            }
        }
    }

    @PreDestroy
    public void close(){
        userService.allUserStop();
    }

}

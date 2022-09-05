package com.joebrooks.showmethecoin.repository.userconfig;

import com.joebrooks.showmethecoin.repository.candle.CandleMinute;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.autotrade.strategy.StrategyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "user_config")
public class UserConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "strategy")
    @Enumerated(EnumType.STRING)
    private StrategyType strategy;

    @Column(name = "candle_minute")
    @Enumerated(EnumType.STRING)
    private CandleMinute candleMinute;

    @Column(name = "allow_sell_with_loss")
    private boolean allowSellWithLoss;

    @Column(name = "is_trading")
    private boolean isTrading;

    @Column(name = "bet_money")
    private double betMoney;

    @Column(name = "max_bet_count")
    private int maxBetCount;

    @Column(name = "order_cancel_minute")
    private int orderCancelMinute;

    @Column(name = "max_trade_coin_count")
    private int maxTradeCoinCount;

    @Column(name = "cash_divided_count")
    private int cashDividedCount;

    @OneToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user")
    private UserEntity user;


    public void setBetMoney(double money){
        this.betMoney = money;
    }

    public void changeStrategy(StrategyType strategy){
        this.strategy = strategy;
    }

    public void changeMaxBetCount(int maxBetCount){
        this.maxBetCount = maxBetCount;
    }

    public void changeMaxTradeCoinCount(int maxTradeCoinCount){
        this.maxTradeCoinCount = maxTradeCoinCount;
    }

    public void changeCandleMinute(CandleMinute candleMinute){
        this.candleMinute = candleMinute;
    }

    public void changeOrderCancelMinute(int orderCancelMinute){
        this.orderCancelMinute = orderCancelMinute;
    }

    public void changeCashDividedCount(int cashDividedCount){
        this.cashDividedCount = cashDividedCount;
    }


    public void setAllowSellWithLoss(boolean allowSellWithLoss){
        this.allowSellWithLoss = allowSellWithLoss;
    }



    public void startTrading(){
        this.isTrading = true;
    }

    public void stopTrading(){
        this.isTrading = false;
    }



}

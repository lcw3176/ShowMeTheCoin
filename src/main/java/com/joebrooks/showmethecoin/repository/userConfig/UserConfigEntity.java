package com.joebrooks.showmethecoin.repository.userConfig;

import com.joebrooks.showmethecoin.global.strategy.BuyStrategy;
import com.joebrooks.showmethecoin.global.strategy.SellStrategy;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.upbit.client.CoinType;
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

    @Column(name = "balance")
    private double balance;

    @Column(name = "trade_coin")
    @Enumerated(EnumType.STRING)
    private CoinType tradeCoin;

    @Column(name = "start_price")
    private double startPrice;

    @Column(name = "difference_level")
    private int differenceLevel;

    @Column(name = "is_trading")
    private boolean isTrading;

    @Column(name = "common_difference")
    private double commonDifference;

    @Column(name = "buy_strategy")
    @Enumerated(EnumType.STRING)
    private BuyStrategy buyStrategy;

    @Column(name = "sell_strategy")
    @Enumerated(EnumType.STRING)
    private SellStrategy sellStrategy;

    @OneToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    public void changeBalance(double balance){
        this.balance = balance;
    }

    public void changeTradeStatus(boolean isTrading){
        this.isTrading = isTrading;
    }

    public void changeTradeCoin(CoinType tradeCoin){
        this.tradeCoin = tradeCoin;
    }

    public void changeDifferenceLevel(int differenceLevel){
        this.differenceLevel = differenceLevel;
    }

    public void changeStartPrice(double startPrice){
        this.startPrice = startPrice;
    }
}

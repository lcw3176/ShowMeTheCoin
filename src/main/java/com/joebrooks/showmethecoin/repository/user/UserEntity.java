package com.joebrooks.showmethecoin.repository.user;


import com.joebrooks.showmethecoin.upbitTrade.upbit.CoinType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(name = "user_pw", unique = true)
    private String userPw;

    @Column(name = "balance")
    private double balance;

    @Column(name = "trade_coin")
    @Enumerated(EnumType.STRING)
    private CoinType tradeCoin;

    @Column(name = "start_price")
    private double startPrice;

    @Column(name = "last_price")
    private double lastPrice;

    @Column(name = "divide_count")
    private long divideCount;

    @Column(name = "is_trading")
    private boolean isTrading;


    public void changeTradeStatus(boolean isTrading){
        this.isTrading = isTrading;
    }
}
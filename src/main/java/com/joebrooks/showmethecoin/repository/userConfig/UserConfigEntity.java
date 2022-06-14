package com.joebrooks.showmethecoin.repository.userConfig;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.trade.upbit.client.CoinType;
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

    @Column(name = "is_trading")
    private boolean isTrading;

    
    @ManyToOne(targetEntity = UserEntity.class)
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


}

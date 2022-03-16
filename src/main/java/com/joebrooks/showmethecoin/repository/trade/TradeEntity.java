package com.joebrooks.showmethecoin.repository.trade;

import com.joebrooks.showmethecoin.global.trade.TradeResult;
import com.joebrooks.showmethecoin.global.trade.TradeStatus;
import com.joebrooks.showmethecoin.repository.BaseTimeEntity;
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
@Entity(name = "trade")
public class TradeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coin_type")
    @Enumerated(EnumType.STRING)
    private CoinType coinType;

    @Column(name = "trade_result")
    @Enumerated(EnumType.STRING)
    private TradeResult tradeResult;

    @Column(name = "buy_price")
    private double buyPrice;

    @Column(name = "sell_price")
    private double sellPrice;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity userId;
}

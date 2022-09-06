package com.joebrooks.showmethecoin.user.tradelog;

import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import com.joebrooks.showmethecoin.trade.CompanyType;
import com.joebrooks.showmethecoin.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "trade_log")
public class TradeLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coin_type")
    @Enumerated(EnumType.STRING)
    private CoinType coinType;

    @Column(name = "company_type")
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Column(name = "trade_result")
    @Enumerated(EnumType.STRING)
    private TradeResult tradeResult;

    @Column(name = "buy_price")
    private double buyPrice;

    @Column(name = "sell_price")
    private double sellPrice;

    @Column(name = "order_end_date")
    private LocalDateTime orderEndDate;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user")
    private UserEntity user;
}

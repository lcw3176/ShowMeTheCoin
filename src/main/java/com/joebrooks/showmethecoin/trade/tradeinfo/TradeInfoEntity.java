package com.joebrooks.showmethecoin.trade.tradeinfo;

import com.joebrooks.showmethecoin.trade.CompanyType;
import com.joebrooks.showmethecoin.user.UserEntity;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
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
@Entity(name = "trade_info")
public class TradeInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coin_type")
    @Enumerated(EnumType.STRING)
    private CoinType coinType;

    @Column(name = "company_type")
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Column(name = "trade_price")
    private double tradePrice;

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;

    @Column(name = "coin_volume")
    private double coinVolume;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "bet_count")
    private int betCount;

    @Column(name = "ordered")
    private boolean ordered;

    @Column(name = "completed")
    private boolean completed;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user")
    private UserEntity user;

    public void complete(){
        this.completed = true;
    }

    public void acceptOrder(){
        this.ordered = true;
        this.orderedAt = LocalDateTime.now();
    }

    public void cancelOrder(){
        this.ordered = false;
    }
}

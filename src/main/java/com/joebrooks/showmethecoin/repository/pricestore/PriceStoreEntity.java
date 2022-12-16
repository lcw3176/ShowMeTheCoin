package com.joebrooks.showmethecoin.repository.pricestore;

import com.joebrooks.showmethecoin.exchange.CommonCoinType;
import com.joebrooks.showmethecoin.exchange.CompanyType;
import com.joebrooks.showmethecoin.repository.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "price_store")
public class PriceStoreEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_type")
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;


    @Column(name = "coin_type")
    @Enumerated(EnumType.STRING)
    private CommonCoinType coinType;


    @Column(name = "last_trade_price")
    private double lastTradePrice;


    public void changeLastTradePrice(double lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }
}

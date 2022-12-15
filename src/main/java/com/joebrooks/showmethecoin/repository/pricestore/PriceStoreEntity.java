package com.joebrooks.showmethecoin.repository.pricestore;

import com.joebrooks.showmethecoin.exchange.CompanyType;
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
public class PriceStoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_type")
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;


    @Column(name = "market")
    private String market;

    @Column(name = "candle_date_time_kst")
    private String dateKst;

    @Column(name = "high_price")
    private Double highPrice;

    @Column(name = "low_price")
    private Double lowPrice;

    @Column(name = "trade_price")
    private Double tradePrice;


}

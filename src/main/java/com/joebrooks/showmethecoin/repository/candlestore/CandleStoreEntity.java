package com.joebrooks.showmethecoin.repository.candlestore;

import com.joebrooks.showmethecoin.trade.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "candle_store")
public class CandleStoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_type")
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Column(name = "candle_minute")
    @Enumerated(EnumType.STRING)
    private CandleMinute candleMinute;

    @Column(name = "market")
    private String market;

    @Column(name = "candle_date_time_utc")
    private String dateUtc;

    @Column(name = "candle_date_time_kst")
    private String dateKst;

    @Column(name = "opening_price")
    private Double openingPrice;

    @Column(name = "high_price")
    private Double highPrice;

    @Column(name = "low_price")
    private Double lowPrice;

    @Column(name = "trade_price")
    private Double tradePrice;

    // 해당 캔들에서 마지막 틱이 저장된 시각
    @Column(name = "timestamp")
    private Long timeStamp;

    // 누적 거래 금액
    @Column(name = "candle_acc_trade_price")
    private Double accTradePrice;

    // 누적 거래량
    @Column(name = "candle_acc_trade_volume")
    private Double accTradeVolume;

    @Column(name = "unit")
    private Integer unit;

    public void changeHighPrice(double highPrice){
        this.highPrice = highPrice;
    }

    public void changeLowPrice(double lowPrice){
        this.lowPrice = lowPrice;
    }

    public void changeTradePrice(double tradePrice){
        this.tradePrice = tradePrice;
    }

    public void changeAccTradePrice(double accTradePrice){
        this.accTradePrice = accTradePrice;
    }

    public void changeAccTradeVolume(double accTradeVolume){
        this.accTradeVolume = accTradeVolume;
    }

    public void changeTimeStamp(long timeStamp){
        this.timeStamp = timeStamp;
    }

}

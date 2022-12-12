package com.joebrooks.showmethecoin.trade.upbit;

import com.joebrooks.showmethecoin.global.util.TimeFormatter;
import com.joebrooks.showmethecoin.trade.CompanyType;
import com.joebrooks.showmethecoin.trade.PriceResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UpBitPrice implements PriceResponse {

    private CompanyType companyType;
    private String market;
    private String tradeTimeKst;
    private String tradeDateKst;
    private double tradePrice;


    @Override
    public CompanyType getCompanyType() {
        return companyType;
    }

    @Override
    public String getMarket() {
        return market;
    }

    @Override
    public LocalDateTime getTradeDateTimeKst() {
        return TimeFormatter.parseTime(tradeDateKst, tradeTimeKst);
    }

    @Override
    public double getTradePrice() {
        return tradePrice;
    }
}

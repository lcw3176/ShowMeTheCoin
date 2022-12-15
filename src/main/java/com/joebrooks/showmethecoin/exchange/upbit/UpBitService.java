package com.joebrooks.showmethecoin.exchange.upbit;

import com.joebrooks.showmethecoin.exchange.CompanyType;
import com.joebrooks.showmethecoin.exchange.PriceResponse;
import com.joebrooks.showmethecoin.exchange.upbit.ticker.TickerResponse;
import com.joebrooks.showmethecoin.exchange.upbit.ticker.UpBitTickerService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpBitService {

    private final UpBitTickerService upBitTickerService;

    public List<PriceResponse> getPrices(List<UpBitCoinType> coinType) {
        List<TickerResponse> tickerResponses = upBitTickerService.getTicker(coinType);

        return tickerResponses.stream().map(response ->
                        UpBitPrice.builder()
                                .market(response.getMarket())
                                .companyType(CompanyType.UPBIT)
                                .tradePrice(response.getTradePrice())
                                .tradeDateKst(response.getTradeDateKst())
                                .tradeTimeKst(response.getTradeTimeKst())
                                .build())
                .collect(Collectors.toList());
    }

    public List<PriceResponse> getCommonPrices() {
        List<TickerResponse> tickerResponses = upBitTickerService.getCommonTicker();

        return tickerResponses.stream().map(response ->
                        UpBitPrice.builder()
                                .market(response.getMarket())
                                .companyType(CompanyType.UPBIT)
                                .tradePrice(response.getTradePrice())
                                .tradeDateKst(response.getTradeDateKst())
                                .tradeTimeKst(response.getTradeTimeKst())
                                .build())
                .collect(Collectors.toList());
    }
}

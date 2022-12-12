package com.joebrooks.showmethecoin.trade.upbit;

import com.joebrooks.showmethecoin.trade.CompanyType;
import com.joebrooks.showmethecoin.trade.PriceResponse;
import com.joebrooks.showmethecoin.trade.upbit.ticker.TickerResponse;
import com.joebrooks.showmethecoin.trade.upbit.ticker.TickerService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpBitUseCase {

    private final TickerService tickerService;


    public List<PriceResponse> getPrices(CoinType... coinTypes){
        List<TickerResponse> tickerResponses = tickerService.getTicker(coinTypes);

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

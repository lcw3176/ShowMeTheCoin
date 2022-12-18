package com.joebrooks.showmethecoin.exchange.coinone;

import com.joebrooks.showmethecoin.exchange.CompanyType;
import com.joebrooks.showmethecoin.exchange.PriceResponse;
import com.joebrooks.showmethecoin.exchange.coinone.ticker.CoinOneTickerService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoinOneService {

    private final CoinOneTickerService coinOneTickerService;


    public List<PriceResponse> getAllPrices() {
        return coinOneTickerService.getTickers().stream().map(i ->
                CoinOnePrice.builder()
                        .market(i.getTargetCurrency())
                        .companyType(CompanyType.COIN_ONE)
                        .tradePrice(i.getBestAsks().get(0).getPrice())
                        .timeStamp(i.getTimestamp())
                        .availableBuyPrice(i.getBestAsks().get(0).getPrice())
                        .availableSellPrice(i.getBestBids().get(0).getPrice())
                        .build()
        ).collect(Collectors.toList());
    }

}

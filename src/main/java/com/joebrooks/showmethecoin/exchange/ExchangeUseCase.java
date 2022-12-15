package com.joebrooks.showmethecoin.exchange;

import com.joebrooks.showmethecoin.exchange.coinone.CoinOneService;
import com.joebrooks.showmethecoin.exchange.upbit.UpBitCoinType;
import com.joebrooks.showmethecoin.exchange.upbit.UpBitService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExchangeUseCase {

    private final UpBitService upBitService;
    private final CoinOneService coinOneService;

    public List<ExchangeResponse> getDuplicatedCoinPrices() {
        List<PriceResponse> upBitPrices = upBitService.getCommonPrices();
        List<PriceResponse> coinOnePrices = coinOneService.getAllPrices();

        List<ExchangeResponse> priceResponses = new ArrayList<>();

        for (PriceResponse upBit : upBitPrices) {
            for (PriceResponse coinOne : coinOnePrices) {
                if (upBit.getMarket().split("-")[1].toLowerCase().equals(coinOne.getMarket())) {
                    priceResponses.add(
                            ExchangeResponse.builder()
                                    .coinName(UpBitCoinType.valueOf(coinOne.getMarket().toUpperCase()).getKoreanName())
                                    .coinOnePrice(coinOne)
                                    .upBitPrice(upBit)
                                    .difference((1 - Math.min(coinOne.getTradePrice(), upBit.getTradePrice())
                                            / Math.max(coinOne.getTradePrice(), upBit.getTradePrice())) * 100)
                                    .lastModified(LocalDateTime.now())
                                    .build());
                }
            }
        }

        return priceResponses.stream()
                .sorted(Comparator.comparing(ExchangeResponse::getDifference, Comparator.reverseOrder()))
                .collect(Collectors.toList());

    }
}

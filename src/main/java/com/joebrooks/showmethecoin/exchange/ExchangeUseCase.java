package com.joebrooks.showmethecoin.exchange;

import com.joebrooks.showmethecoin.exchange.bithumb.BiThumbService;
import com.joebrooks.showmethecoin.exchange.coinone.CoinOneService;
import com.joebrooks.showmethecoin.exchange.upbit.UpBitPrice;
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
    private final BiThumbService biThumbService;

    public List<ExchangeResponse> getDuplicatedCoinPrices() {
        List<PriceResponse> upBitPrices = upBitService.getCommonPrices();
        List<PriceResponse> coinOnePrices = coinOneService.getAllPrices();
//        List<PriceResponse> biThumbPrices = biThumbService.getAllPrices();

        List<ExchangeResponse> priceResponses = new ArrayList<>();

        for (CommonCoinType coinType : CommonCoinType.values()) {
            PriceResponse upBit = upBitPrices.stream()
                    .filter(i -> i.getMarket().equals(coinType.toString().toLowerCase()))
                    .findFirst().orElse(UpBitPrice.builder()
                            .market(coinType.toString())
                            .companyType(CompanyType.UPBIT)
                            .tradePrice(0D)
                            .tradeDateKst(LocalDateTime.now().toString())
                            .build());

            PriceResponse coinOne = coinOnePrices.stream()
                    .filter(i -> i.getMarket().equals(coinType.toString().toLowerCase()))
                    .findFirst().orElse(UpBitPrice.builder()
                            .market(coinType.toString())
                            .companyType(CompanyType.COIN_ONE)
                            .tradePrice(0D)
                            .tradeDateKst(LocalDateTime.now().toString())
                            .build());

//            PriceResponse biThumb = biThumbPrices.stream()
//                    .filter(i -> i.getMarket().equals(coinType.toString()))
//                    .findFirst().orElse(BiThumbPrice.builder()
//                            .market(coinType.toString())
//                            .companyType(CompanyType.BITHUMB)
//                            .tradePrice(0D)
//                            .build());

            priceResponses.add(
                    ExchangeResponse.builder()
                            .coinId(coinType.toString())
                            .coinKoreanName(coinType.getName())
                            .coinOnePrice(ExchangeUtil.priceFormatter(coinOne.getTradePrice()))
//                            .biThumbPrice(ExchangeUtil.priceFormatter(biThumb.getTradePrice()))
                            .upBitPrice(ExchangeUtil.priceFormatter(upBit.getTradePrice()))
                            .difference((1 - coinOne.getTradePrice()
                                    / upBit.getTradePrice()) * 100)
                            .lastModified(LocalDateTime.now())
                            .build());
        }

        return priceResponses.stream()
                .sorted(Comparator.comparing(ExchangeResponse::getDifference, Comparator.reverseOrder()))
                .collect(Collectors.toList());

    }
}

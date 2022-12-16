package com.joebrooks.showmethecoin.exchange;

import com.joebrooks.showmethecoin.exchange.coinone.CoinOneService;
import com.joebrooks.showmethecoin.exchange.upbit.UpBitService;
import com.joebrooks.showmethecoin.repository.pricestore.PriceStoreEntity;
import com.joebrooks.showmethecoin.repository.pricestore.PriceStoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExchangeRefresher {
    private final UpBitService upBitService;
    private final CoinOneService coinOneService;
    private final PriceStoreRepository priceStoreRepository;

    @Scheduled(fixedDelay = 5000)
    public void refreshCommonCoinsPrice() {
        List<PriceResponse> priceResponses = upBitService.getCommonPrices();
        priceResponses.addAll(coinOneService.getAllPrices());

        for (CommonCoinType coinType : CommonCoinType.values()) {
            for (CompanyType companyType : CompanyType.values()) {
                if (companyType == CompanyType.BITHUMB) {
                    continue;
                }

                double price = priceResponses.stream()
                        .filter(i -> i.getMarket().equals(coinType.toString().toLowerCase())
                                && i.getCompanyType() == companyType)
                        .mapToDouble(PriceResponse::getTradePrice)
                        .findFirst()
                        .orElse(0D);

                PriceStoreEntity priceStoreEntity = priceStoreRepository
                        .findByCoinTypeAndCompanyType(coinType, companyType)
                        .orElse(PriceStoreEntity.builder()
                                .companyType(companyType)
                                .coinType(coinType)
                                .lastTradePrice(0D)
                                .build());

                priceStoreEntity.changeLastTradePrice(price);
                priceStoreRepository.save(priceStoreEntity);
            }

        }
    }
}

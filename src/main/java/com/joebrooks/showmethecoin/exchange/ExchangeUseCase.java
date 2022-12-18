package com.joebrooks.showmethecoin.exchange;

import com.joebrooks.showmethecoin.repository.pricestore.PriceStoreEntity;
import com.joebrooks.showmethecoin.repository.pricestore.PriceStoreRepository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExchangeUseCase {

    private final PriceStoreRepository priceStoreRepository;

    public List<ExchangeResponse> getPriceWhenBuyFromCoinOne() {
        List<PriceStoreEntity> entities = priceStoreRepository.findAll();
        Map<CommonCoinType, Map<CompanyType, Double>> prices = new HashMap<>();

        for (CommonCoinType coinType : CommonCoinType.values()) {
            prices.put(coinType, new HashMap<>());
        }

        for (PriceStoreEntity entity : entities) {
            if (entity.getCompanyType() == CompanyType.COIN_ONE) {
                prices.get(entity.getCoinType())
                        .put(entity.getCompanyType(), entity.getAvailableBuy());
            } else {
                prices.get(entity.getCoinType())
                        .put(entity.getCompanyType(), entity.getLastTradePrice());
            }

        }

        return entities.stream()
                .filter(distinctByKey(PriceStoreEntity::getCoinType))
                .map(entity ->
                        ExchangeResponse.builder()
                                .coinId(entity.getCoinType().toString())
                                .coinKoreanName(entity.getCoinType().getKoreanName())
                                .prices(ExchangeUtil.priceFormatter(prices.get(entity.getCoinType())))
                                .difference(
                                        (1 - prices.get(entity.getCoinType()).get(CompanyType.COIN_ONE) / prices.get(
                                                entity.getCoinType()).get(CompanyType.UPBIT)) * 100)
                                .lastModified(entity.getLastModified())
                                .build())
                .sorted(Comparator.comparing(ExchangeResponse::getDifference, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<ExchangeResponse> getPriceWhenBuyFromUpBit() {
        List<PriceStoreEntity> entities = priceStoreRepository.findAll();
        Map<CommonCoinType, Map<CompanyType, Double>> prices = new HashMap<>();

        for (CommonCoinType coinType : CommonCoinType.values()) {
            prices.put(coinType, new HashMap<>());
        }

        for (PriceStoreEntity entity : entities) {
            if (entity.getCompanyType() == CompanyType.COIN_ONE) {
                prices.get(entity.getCoinType())
                        .put(entity.getCompanyType(), entity.getAvailableSell());
            } else {
                prices.get(entity.getCoinType())
                        .put(entity.getCompanyType(), entity.getLastTradePrice());
            }
        }

        return entities.stream()
                .filter(distinctByKey(PriceStoreEntity::getCoinType))
                .map(entity ->
                        ExchangeResponse.builder()
                                .coinId(entity.getCoinType().toString())
                                .coinKoreanName(entity.getCoinType().getKoreanName())
                                .prices(ExchangeUtil.priceFormatter(prices.get(entity.getCoinType())))
                                .difference(
                                        (1 - prices.get(entity.getCoinType()).get(CompanyType.UPBIT) / prices.get(
                                                entity.getCoinType()).get(CompanyType.COIN_ONE)) * 100)
                                .lastModified(entity.getLastModified())
                                .build())
                .sorted(Comparator.comparing(ExchangeResponse::getDifference, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}

package com.joebrooks.showmethecoin.exchange;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.joebrooks.showmethecoin.exchange.bithumb.BiThumbPrice;
import com.joebrooks.showmethecoin.exchange.bithumb.BiThumbService;
import com.joebrooks.showmethecoin.exchange.coinone.CoinOneService;
import com.joebrooks.showmethecoin.exchange.upbit.UpBitPrice;
import com.joebrooks.showmethecoin.exchange.upbit.UpBitService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExchangeUseCase {

	private final UpBitService upBitService;
	private final CoinOneService coinOneService;
	private final BiThumbService biThumbService;

	public List<ExchangeResponse> getDuplicatedCoinPrices() {
		List<PriceResponse> upBitPrices = upBitService.getCommonPrices();
		List<PriceResponse> coinOnePrices = coinOneService.getAllPrices();
		List<PriceResponse> biThumbPrices = biThumbService.getAllPrices();

		System.out.println(biThumbPrices);
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

			PriceResponse biThumb = biThumbPrices.stream()
				.filter(i -> i.getMarket().equals(coinType.toString()))
				.findFirst().orElse(BiThumbPrice.builder()
					.market(coinType.toString())
					.companyType(CompanyType.BITHUMB)
					.tradePrice(0D)
					.build());

			priceResponses.add(
				ExchangeResponse.builder()
					.coinName(coinType.toString())
					.coinOnePrice(coinOne)
					.biThumbPrice(biThumb)
					.upBitPrice(upBit)
					.difference((1 - Math.min(coinOne.getTradePrice(), upBit.getTradePrice())
						/ Math.max(coinOne.getTradePrice(), upBit.getTradePrice())) * 100)
					.lastModified(LocalDateTime.now())
					.build());
		}

		// for (PriceResponse upBit : upBitPrices) {
		// 	for (PriceResponse coinOne : coinOnePrices) {
		// 		if (upBit.getMarket().split("-")[1].toLowerCase().equals(coinOne.getMarket())) {
		// 			priceResponses.add(
		// 				ExchangeResponse.builder()
		// 					.coinName(UpBitCoinType.valueOf(coinOne.getMarket().toUpperCase()).getKoreanName())
		// 					.coinOnePrice(coinOne)
		// 					.upBitPrice(upBit)
		// 					.difference((1 - Math.min(coinOne.getTradePrice(), upBit.getTradePrice())
		// 						/ Math.max(coinOne.getTradePrice(), upBit.getTradePrice())) * 100)
		// 					.lastModified(LocalDateTime.now())
		// 					.build());
		// 		}
		// 	}
		// }

		return priceResponses.stream()
			.sorted(Comparator.comparing(ExchangeResponse::getDifference, Comparator.reverseOrder()))
			.collect(Collectors.toList());

	}
}

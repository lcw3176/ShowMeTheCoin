package com.joebrooks.showmethecoin.exchange.bithumb;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.joebrooks.showmethecoin.exchange.CompanyType;
import com.joebrooks.showmethecoin.exchange.PriceResponse;
import com.joebrooks.showmethecoin.exchange.bithumb.ticker.BiThumbTickerService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BiThumbService {

	private final BiThumbTickerService biThumbTickerService;

	public List<PriceResponse> getAllPrices() {
		return biThumbTickerService.getTickers().stream().map(i ->
			BiThumbPrice.builder()
				.market(i.getMarket())
				.companyType(CompanyType.BITHUMB)
				.tradePrice(i.getTradePrice())
				.build()
		).collect(Collectors.toList());
	}

}

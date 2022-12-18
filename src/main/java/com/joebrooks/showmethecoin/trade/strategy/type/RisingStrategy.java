package com.joebrooks.showmethecoin.trade.strategy.type;

import java.util.List;

import org.springframework.stereotype.Component;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.strategy.IStrategy;
import com.joebrooks.showmethecoin.trade.strategy.policy.PolicyService;
import com.joebrooks.showmethecoin.trade.strategy.policy.rising.buy.RisingBuyUsingBollingerBands;
import com.joebrooks.showmethecoin.trade.strategy.policy.rising.buy.RisingBuyUsingMacd;
import com.joebrooks.showmethecoin.trade.strategy.policy.rising.sell.RisingSellUsingBollingerBands;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RisingStrategy implements IStrategy {

	private final PolicyService policyService;

	@Override
	public boolean isProperToBuy(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {

		return policyService
			.getBuyPolicy(RisingBuyUsingMacd.class, RisingBuyUsingBollingerBands.class)
			.stream()
			.allMatch(i -> i.isProperToBuy(candleResponses, tradeInfo));
	}

	@Override
	public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {

		return policyService
			.getSellPolicy(RisingSellUsingBollingerBands.class)
			.stream()
			.allMatch(i -> i.isProperToSellWithBenefit(candleResponses, tradeInfo));
	}

	@Override
	public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
		// return policyService
		// 	.getSellPolicy(RisingSellUsingMacd.class)
		// 	.stream()
		// 	.allMatch(i -> i.isProperToSellWithLoss(candleResponses, tradeInfo));
		return true;
	}
}

package com.joebrooks.showmethecoin.trade.strategy.policy.rising.sell;

import java.util.List;

import org.springframework.stereotype.Component;

import com.joebrooks.showmethecoin.repository.candlestore.CandleStoreEntity;
import com.joebrooks.showmethecoin.repository.tradeinfo.TradeInfoEntity;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdIndicator;
import com.joebrooks.showmethecoin.trade.indicator.macd.MacdResponse;
import com.joebrooks.showmethecoin.trade.strategy.policy.ISellPolicy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RisingSellUsingMacd implements ISellPolicy {

	private final MacdIndicator macdIndicator;

	@Override
	public boolean isProperToSellWithBenefit(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
		List<MacdResponse> macdResponseList = macdIndicator.getMacd(candleResponses);

		return macdResponseList.get(0).getMacd() > 0
			&& macdResponseList.get(0).getSignal() > 0
			&& macdResponseList.get(1).getMacd() > macdResponseList.get(1).getSignal()
			&& macdResponseList.get(0).getMacd() < macdResponseList.get(0).getSignal();
	}

	@Override
	public boolean isProperToSellWithLoss(List<CandleStoreEntity> candleResponses, List<TradeInfoEntity> tradeInfo) {
		return true;
	}
}

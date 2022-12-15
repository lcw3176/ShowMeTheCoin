package com.joebrooks.showmethecoin.exchange.bithumb.ticker;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TickerResponse {

	@JsonProperty("closing_price")
	private double tradePrice;

	private String market;

	public void setMarket(String market) {
		this.market = market;
	}
}

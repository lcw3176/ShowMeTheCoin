package com.joebrooks.showmethecoin.exchange.bithumb.ticker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ticker {

	private String status;
	private String data;
}

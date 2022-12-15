package com.joebrooks.showmethecoin.exchange.bithumb.ticker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BiThumbTickerServiceTest {

	@Autowired
	private BiThumbTickerService biThumbTickerService;

	@Test
	void 가격_불러오기() {
		System.out.println(biThumbTickerService.getTickers());
	}
}

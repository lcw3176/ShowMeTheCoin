package com.joebrooks.showmethecoin.exchange.coinone;

import static org.assertj.core.api.Assertions.assertThat;

import com.joebrooks.showmethecoin.exchange.CompanyType;
import com.joebrooks.showmethecoin.exchange.PriceResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CoinOneServiceTest {

    @Autowired
    private CoinOneService coinOneService;


    @Test
    void 가격_정보_가져오기() {
        List<PriceResponse> coinOnePrices = coinOneService.getAllPrices();

        for (PriceResponse i : coinOnePrices) {
            assertThat(i.getCompanyType()).isEqualTo(CompanyType.COIN_ONE);
            assertThat(i.getTradePrice()).isPositive();
        }
    }


}
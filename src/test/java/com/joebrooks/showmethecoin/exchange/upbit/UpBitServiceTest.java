package com.joebrooks.showmethecoin.exchange.upbit;


import static org.assertj.core.api.Assertions.assertThat;

import com.joebrooks.showmethecoin.exchange.CompanyType;
import com.joebrooks.showmethecoin.exchange.PriceResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UpBitServiceTest {

    @Autowired
    private UpBitService upBitService;


    @Test
    void 현재_업비트_코인_정보_가져오기() {
        List<PriceResponse> candleResponse = upBitService.getPrices(List.of(UpBitCoinType.BTC, UpBitCoinType.ETH));

        candleResponse.forEach(i -> {
            assertThat(i.getCompanyType()).isEqualTo(CompanyType.UPBIT);
            assertThat(i.getMarket()).isIn(Arrays.stream(UpBitCoinType.values())
                    .map(UpBitCoinType::getName)
                    .collect(Collectors.toList()));
        });

    }
}
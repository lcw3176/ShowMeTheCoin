package com.joebrooks.showmethecoin.trade.upbit;


import static org.assertj.core.api.Assertions.assertThat;

import com.joebrooks.showmethecoin.trade.CompanyType;
import com.joebrooks.showmethecoin.trade.PriceResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UpBitUseCaseTest {

    @Autowired
    private UpBitUseCase upBitUseCase;


    @Test
    void 현재_업비트_코인_정보_가져오기() {
        List<PriceResponse> candleResponse = upBitUseCase.getPrices(CoinType.BTC, CoinType.ETH);

        candleResponse.forEach(i -> {
            assertThat(i.getCompanyType()).isEqualTo(CompanyType.UPBIT);
            assertThat(i.getMarket()).isIn(Arrays.stream(CoinType.values())
                    .map(CoinType::getName)
                    .collect(Collectors.toList()));
        });

    }
}
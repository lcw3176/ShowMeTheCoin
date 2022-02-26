package com.joebrooks.showmethecoin.application.indicator;

import com.joebrooks.showmethecoin.domain.indicator.Indicator;
import com.joebrooks.showmethecoin.infra.upbit.coin.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IndicatorService {

    private final ApplicationContext context;

    public Indicator execute(String strategy, CoinType coinType) {
        IIndicator iIndicator = (IIndicator) context.getBean(strategy);

        return iIndicator.execute(coinType);
    }
}

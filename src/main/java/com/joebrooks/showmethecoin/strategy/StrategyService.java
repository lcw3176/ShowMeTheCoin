package com.joebrooks.showmethecoin.strategy;

import com.joebrooks.showmethecoin.common.upbit.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StrategyService {

    private final ApplicationContext context;

    public Strategy execute(String strategy, CoinType coinType) {
        IStrategy iStrategy = (IStrategy) context.getBean(strategy);

        return iStrategy.execute(coinType);
    }
}

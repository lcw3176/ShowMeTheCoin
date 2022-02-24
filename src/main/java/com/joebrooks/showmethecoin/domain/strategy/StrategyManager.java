package com.joebrooks.showmethecoin.domain.strategy;

import com.joebrooks.showmethecoin.domain.strategy.strategyType.IStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class StrategyManager {

    private final ApplicationContext context;

    public HashMap<String, Double> execute(String strategy) {
        IStrategy iStrategy = (IStrategy) context.getBean(strategy);
        return iStrategy.execute();
    }
}

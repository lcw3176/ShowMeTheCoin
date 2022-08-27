package com.joebrooks.showmethecoin.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StrategyService {

    private final ApplicationContext applicationContext;

    public IStrategy get(StrategyType strategyType) {

        return (IStrategy) applicationContext.getBean(strategyType.toString());
    }

}

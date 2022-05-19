package com.joebrooks.showmethecoin.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StrategyService {

    private final ApplicationContext context;

    public IStrategy get(Strategy strategy) {
        return (IStrategy) context.getBean(strategy.toString());
    }

}

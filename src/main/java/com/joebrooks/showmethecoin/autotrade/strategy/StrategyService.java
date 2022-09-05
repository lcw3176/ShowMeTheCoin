package com.joebrooks.showmethecoin.autotrade.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StrategyService {

    private final ApplicationContext applicationContext;

    public List<IStrategy> get(StrategyType... strategyType) {

        return Arrays.stream(strategyType)
                .map(i -> (IStrategy) applicationContext.getBean(i.getClazz()))
                .collect(Collectors.toList());
    }

}

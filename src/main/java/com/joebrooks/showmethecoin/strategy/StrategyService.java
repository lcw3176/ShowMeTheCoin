package com.joebrooks.showmethecoin.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StrategyService {


    public IStrategy get(StrategyType strategyType) {
        String prefix = "com.joebrooks.showmethecoin.strategy.";

        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(strategyType.toString());

        try{
            return (IStrategy) Class.forName(sb.toString()).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("해당되는 전략 클래스가 존재하지 않습니다", e);
        }
    }

}

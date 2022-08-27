package com.joebrooks.showmethecoin.strategy;

import lombok.Getter;

@Getter
public enum StrategyType {
    baseStrategy("기본적인 매매 전략, 필수 포함"),
    chaserStrategy("추격 매매 전략"),
    shortStrategy("저가 탐색 기반 단타 전략");
    private String description;

    private StrategyType(String description){
        this.description = description;
    }
}

package com.joebrooks.showmethecoin.domain.strategy.strategyType;

import java.util.HashMap;

public interface IStrategy {
    HashMap<String, Double> execute();
}

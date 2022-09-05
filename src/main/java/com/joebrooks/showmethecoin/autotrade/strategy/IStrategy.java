package com.joebrooks.showmethecoin.autotrade.strategy;

import com.joebrooks.showmethecoin.autotrade.strategy.policy.IBuyPolicy;
import com.joebrooks.showmethecoin.autotrade.strategy.policy.ISellPolicy;


public interface IStrategy extends IBuyPolicy, ISellPolicy {

}

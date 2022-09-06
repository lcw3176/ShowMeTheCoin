package com.joebrooks.showmethecoin.trade.strategy;

import com.joebrooks.showmethecoin.trade.strategy.policy.IBuyPolicy;
import com.joebrooks.showmethecoin.trade.strategy.policy.ISellPolicy;


public interface IStrategy extends IBuyPolicy, ISellPolicy {

}

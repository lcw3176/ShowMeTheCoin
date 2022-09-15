package com.joebrooks.showmethecoin.trade.autotrade;

import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter
@UtilityClass
public class TradingCoinList {

    public static final List<CoinType> WHITELIST = Collections.synchronizedList(new LinkedList<>());
    public static final List<CoinType> BLACKLIST = new LinkedList<>();
}

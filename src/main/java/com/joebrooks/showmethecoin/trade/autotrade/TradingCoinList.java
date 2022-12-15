package com.joebrooks.showmethecoin.trade.autotrade;

import com.joebrooks.showmethecoin.exchange.upbit.UpBitCoinType;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@Getter
@UtilityClass
public class TradingCoinList {

    public static final List<UpBitCoinType> WHITELIST = Collections.synchronizedList(new LinkedList<>());
    public static final List<UpBitCoinType> BLACKLIST = new LinkedList<>();
}

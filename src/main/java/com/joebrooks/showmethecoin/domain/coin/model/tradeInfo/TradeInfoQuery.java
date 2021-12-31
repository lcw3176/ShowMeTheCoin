package com.joebrooks.showmethecoin.domain.coin.model.tradeInfo;

public enum TradeInfoQuery {
    RequestCoinInfo("markets");

    private final String url;

    TradeInfoQuery(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}

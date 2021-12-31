package com.joebrooks.showmethecoin.domain.coin.model.tradeInfo;

public enum TradeInfoUri {
    RequestCoinInfo("/v1/ticker");

    private final String url;

    TradeInfoUri(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }

}

package com.joebrooks.showmethecoin.domain.coin.model.order;

public enum OrderUri {
    RequestOrder("/v1/orders"),
    CancelOrder("/v1/order");

    private final String url;

    OrderUri(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }

}

package com.joebrooks.showmethecoin.domain.coin.model.order;

public enum OrderType {
    RequestOrder("RequestOrder"),
    CancelOrder("CancelOrder");

    private final String type;

    OrderType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}

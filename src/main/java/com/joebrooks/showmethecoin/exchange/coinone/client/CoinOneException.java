package com.joebrooks.showmethecoin.exchange.coinone.client;

public class CoinOneException extends RuntimeException {

    public CoinOneException(String msg) {
        super(msg);
    }

    public CoinOneException(String msg, Throwable e) {
        super(msg, e);
    }
}

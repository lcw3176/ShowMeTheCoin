package com.joebrooks.showmethecoin.exchange.upbit.client;

public class UpBitException extends RuntimeException {

    public UpBitException(String msg) {
        super(msg);
    }

    public UpBitException(String msg, Throwable e) {
        super(msg, e);
    }
}

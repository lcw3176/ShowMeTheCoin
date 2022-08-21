package com.joebrooks.showmethecoin.trade.upbit.client;

public class UpBitException extends RuntimeException{

    public UpBitException(String msg, Throwable e){
        super(msg, e);
    }
}

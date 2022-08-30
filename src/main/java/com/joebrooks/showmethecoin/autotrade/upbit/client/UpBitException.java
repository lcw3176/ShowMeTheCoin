package com.joebrooks.showmethecoin.autotrade.upbit.client;

public class UpBitException extends RuntimeException{

    public UpBitException(String msg){
        super(msg);
    }

    public UpBitException(String msg, Throwable e){
        super(msg, e);
    }
}

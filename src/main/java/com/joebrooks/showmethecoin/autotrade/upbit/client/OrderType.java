package com.joebrooks.showmethecoin.autotrade.upbit.client;

import lombok.Getter;

@Getter
public enum OrderType {

    // 지정가 주문
    limit("지정가"),

    // 시장가 매수
    price("매수"),

    //시장가 매도
    market("매도");

    private String value;

    private OrderType(String value){
        this.value = value;
    }
}

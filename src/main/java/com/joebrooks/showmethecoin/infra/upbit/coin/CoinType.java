package com.joebrooks.showmethecoin.infra.upbit.coin;

import lombok.Getter;

@Getter
public enum CoinType {
    XRP("KRW-XRP"),
    ELF("KRW-ELF"),
    GLM("KRW-GLM"),
    AERGO("KRW-AERGO"),
    POWER("KRW-POWR"),
    HUNT("KRW-HUNT"),
    NU("KRW-NU"),
    DOGE("KRW-DOGE");

    private String name;

    private CoinType(String name){
        this.name = name;
    }
}

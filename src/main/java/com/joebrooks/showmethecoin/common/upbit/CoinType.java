package com.joebrooks.showmethecoin.common.upbit;

import lombok.Getter;

@Getter
public enum CoinType {
    BTC("KRW-BTC", "비트코인"),
    ETH("KRW-ETH", "이더리움"),
    CBK("KRW-CBK", "코박토큰"),
    XRP("KRW-XRP", "리플"),
    ELF("KRW-ELF", "엘프"),
    GLM("KRW-GLM", "골렘"),
    AERGO("KRW-AERGO", "아르고"),
    POWER("KRW-POWR", "파워렛저"),
    HUNT("KRW-HUNT", "헌트"),
    NU("KRW-NU", "누사이퍼"),
    BTT("KRW-BTT", "비트토렌트"),
    ADA("KRW-ADA", "에이다"),
    TRX("KRW-TRX", "트론"),
    SAND("KRW-SAND", "샌드박스"),
    PUNDIX("KRW-PUNDIX", "펀디엑스"),
    WAVES("KRW-WAVES", "웨이브"),
    DOGE("KRW-DOGE", "도지코인");

    private String name;
    private String koreanName;

    private CoinType(String name, String koreanName){
        this.name = name;
        this.koreanName = koreanName;
    }
}

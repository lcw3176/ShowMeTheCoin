package com.joebrooks.showmethecoin.trade.upbit.client;

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
    JST("KRW-JST", "저스트"),
    POLY("KRW-POLY", "폴리매쓰"),
    ANKR("KRW-ANKR", "앤커"),
    MBL("KRW-MBL", "무비블록"),
    MFT("KRW-MFT", "메인프레임"),
    CRE("KRW-CRE", "캐리프로토콜"),
    KNC("KRW-KNC", "카이버네트워크"),
    ETC("KRW-ETC", "이더리움클래식"),
    BTG("KRW-BTG", "비트코인골드"),
    QTUM("KRW-QTUM", "퀀텀"),
    SRM("KRW-SRM", "세럼"),
    BORA("KRW-BORA", "보라"),
    MANA("KRW-MANA", "디센트럴랜드"),
    DOGE("KRW-DOGE", "도지코인");

    private String name;
    private String koreanName;

    private CoinType(String name, String koreanName){
        this.name = name;
        this.koreanName = koreanName;
    }
}

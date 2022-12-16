package com.joebrooks.showmethecoin.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonCoinType {
    BTC("비트코인"),
    ETH("이더리움"),
    DOGE("도지"),
    BCH("비트코인캐시"),
    AAVE("에이브"),
    BSV("비트코인에스브이"),
    SOL("솔라나"),
    ETC("이더리움클래식"),
    AVAX("아발란체"),
    AXS("엑시인피니티"),
    ATOM("코스모스"),
    NEO("네오"),
    DOT("폴카닷"),
    LINK("체인링크"),
    QTUM("퀀텀"),
    GAS("가스"),
    FLOW("플로우"),
    OMG("오미세고"),
    KAVA("카바"),
    XTZ("테조스"),
    KNC("카이버네트워크"),
    SAND("샌드박스"),
    EOS("이오스"),
    CBK("코박토큰"),
    MANA("디센트럴랜드"),
    SRM("세럼"),
    MATIC("폴리곤"),
    ADA("에이다"),
    SXP("솔라"),
    ONG("온톨로지가스"),
    BAT("베이직어텐션토큰"),
    MLK("밀크"),
    XRP("리플"),
    ZRX("제로엑스"),
    BORA("보라"),
    ICX("아이콘"),
    IOTA("아이오타"),
    ONT("온톨로지"),
    HUM("휴먼스케이프"),
    CRO("크로노스"),
    XLM("스텔라루멘"),
    TRX("트론"),
    STPT("에스티피"),
    ANKR("앤커"),
    ORBS("오브스"),
    ZIL("질리카"),
    JST("저스트"),
    MBL("무비블록"),
    XEC("이캐시");

    private String koreanName;

}

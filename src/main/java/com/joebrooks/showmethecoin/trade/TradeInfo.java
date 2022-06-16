package com.joebrooks.showmethecoin.trade;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TradeInfo {

    private Double tradePrice;
    private String dateKst;
    private Double coinVolume;
    private String uuid;

}

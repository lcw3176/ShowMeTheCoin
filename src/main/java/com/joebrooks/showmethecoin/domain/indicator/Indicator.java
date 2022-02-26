package com.joebrooks.showmethecoin.domain.indicator;

import lombok.Data;

@Data
public class Indicator<T> {
    private String type;
    private Double tradePrice;
    private String dateKst;
    private T detailInfo;
}

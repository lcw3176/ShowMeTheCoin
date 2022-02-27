package com.joebrooks.showmethecoin.strategy;

import lombok.Data;

@Data
public class Strategy<T> {
    private String type;
    private Double tradePrice;
    private String dateKst;
    private RecommendAction recommend;
    private T detailInfo;
}

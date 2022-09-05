package com.joebrooks.showmethecoin.repository.candle;

import lombok.Getter;

@Getter
public enum CandleMinute {
    ONE_M("1분", 1),
    THREE_M("3분", 3),
    FIVE_M("5분", 5),
    TEN_M("10분", 10),
    FIFTH_M("15분", 15),
    THIRTY_M("30분", 30),

    ONE_H("1시간", 60),
    FOUR_H("4시간", 240);

    private final String description;
    private final int value;

    private CandleMinute(String description, int value){
        this.description = description;
        this.value = value;
    }
}

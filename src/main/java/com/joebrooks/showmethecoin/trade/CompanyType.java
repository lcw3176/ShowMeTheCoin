package com.joebrooks.showmethecoin.trade;

import lombok.Getter;

@Getter
public enum CompanyType {
    UPBIT(5100);

    private int minBetMoney;

    private CompanyType(int minBetMoney){
        this.minBetMoney = minBetMoney;
    }
}

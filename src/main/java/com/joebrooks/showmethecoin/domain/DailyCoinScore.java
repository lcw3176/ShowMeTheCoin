package com.joebrooks.showmethecoin.domain;

import lombok.Data;

@Data
public class DailyCoinScore {
    private static float buyingMoney;
    private static float sellingMoney;

    public static float getBenefit() {
        return sellingMoney - buyingMoney;
    }


    public static float getBuyingMoney() {
        return buyingMoney;
    }

    public static void setBuyingMoney(float buyingMoney) {
        DailyCoinScore.buyingMoney = buyingMoney;
    }

    public static float getSellingMoney() {
        return sellingMoney;
    }

    public static void setSellingMoney(float sellingMoney) {
        DailyCoinScore.sellingMoney = sellingMoney;
    }

    public static void initData(){
        buyingMoney = 0;
        sellingMoney = 0;
    }

}

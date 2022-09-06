package com.joebrooks.showmethecoin.global.fee;

import com.joebrooks.showmethecoin.trade.CompanyType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FeeCalculator {

    public double calculate(double tradePrice, double coinVolume, CompanyType companyType){
        if(companyType == CompanyType.UPBIT){
            return tradePrice * coinVolume * 0.0005;
        } else {
            throw new RuntimeException("수수료 계산 에러");
        }

    }
}

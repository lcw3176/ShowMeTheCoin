package com.joebrooks.showmethecoin.trade.upbit;

import org.junit.jupiter.api.Test;

class UpbitUtilTest {

    @Test
    void 단위(){
        System.out.println(UpbitUtil.getTickSize(2275));
    }
}
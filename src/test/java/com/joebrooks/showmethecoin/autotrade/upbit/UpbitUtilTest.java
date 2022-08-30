package com.joebrooks.showmethecoin.autotrade.upbit;

import org.junit.jupiter.api.Test;

class UpbitUtilTest {

    @Test
    public void 단위(){
        System.out.println(UpbitUtil.getTickSize(2275));
    }
}
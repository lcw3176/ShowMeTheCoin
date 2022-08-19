package com.joebrooks.showmethecoin.trade.upbit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpbitUtilTest {

    @Test
    public void 단위(){
        System.out.println(UpbitUtil.getTickSize(2275));
    }
}
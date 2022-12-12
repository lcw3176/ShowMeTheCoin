package com.joebrooks.showmethecoin.trade.upbit;

import org.junit.jupiter.api.Test;

class UpBitUtilTest {

    @Test
    void 단위(){
        System.out.println(UpBitUtil.getTickSize(2275));
    }
}
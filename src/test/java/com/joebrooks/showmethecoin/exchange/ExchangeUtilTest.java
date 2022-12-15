package com.joebrooks.showmethecoin.exchange;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ExchangeUtilTest {


    @Test
    void 숫자_포맷팅_테스트() {
        assertThat(ExchangeUtil.priceFormatter(123D)).isEqualTo("123");
        assertThat(ExchangeUtil.priceFormatter(123.123D)).isEqualTo("123.123");
        assertThat(ExchangeUtil.priceFormatter(0.00123D)).isEqualTo("0.00123");
    }
}
package com.joebrooks.showmethecoin.upbit.order;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CheckOrderResponse {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("side")
    private String side;

    @JsonProperty("price")
    private String price;

    @JsonProperty("market")
    private String market;

    @JsonProperty("avg_price")
    private String avgPrice;

    @JsonProperty("created_at")
    private String createdAt;

    // 체결된 양
    @JsonProperty("executed_volume")
    private String executeVolume;

    // 거래에 사용중인 비용
    @JsonProperty("locked")
    private String locked;
    
    // 사용된 수수료
    @JsonProperty("paid_fee")
    private String paidFee;

    // 해당 주문에 걸린 체결 수
    @JsonProperty("trade_count")
    private int tradeCount;

    public void setPriceByPaidFee(){
        this.price = BigDecimal.valueOf(Double.parseDouble(this.paidFee) / 0.0005).toString();
    }
}

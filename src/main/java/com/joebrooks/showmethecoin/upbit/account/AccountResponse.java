package com.joebrooks.showmethecoin.upbit.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AccountResponse {
    @JsonProperty("currency")
    private String currency;

    @JsonProperty("balance")
    private String balance;

    @JsonProperty("locked")
    private String locked;

    @JsonProperty("avg_buy_price")
    private String avgBuyPrice;

    @JsonProperty("avg_buy_price_modified")
    private boolean avgBuyPriceModified;

    @JsonProperty("unit_currency")
    private String unitCurrency;
}

package com.joebrooks.showmethecoin.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderResponse {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("price")
    private String price;

    @JsonProperty("market")
    private String market;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("executed_volume")
    private String executeVolume;

}

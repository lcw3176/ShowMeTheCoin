package com.joebrooks.showmethecoin.user.tradeinfo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradePageResponse {

    private int startPage;
    private int lastPage;
    private int nowPage;
    private int nextPage;
    private int previousPage;
}

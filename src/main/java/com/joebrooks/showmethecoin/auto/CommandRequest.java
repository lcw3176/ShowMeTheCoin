package com.joebrooks.showmethecoin.auto;

import com.joebrooks.showmethecoin.common.upbit.CoinType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandRequest {
    private AutoCommand autoCommand;
    private String strategy;
    private CoinType coinType;
}

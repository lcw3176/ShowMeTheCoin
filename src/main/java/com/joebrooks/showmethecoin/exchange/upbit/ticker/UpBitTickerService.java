package com.joebrooks.showmethecoin.exchange.upbit.ticker;

import com.joebrooks.showmethecoin.exchange.CommonCoinType;
import com.joebrooks.showmethecoin.exchange.upbit.UpBitCoinType;
import com.joebrooks.showmethecoin.exchange.upbit.client.UpBitClient;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class UpBitTickerService {


    private final UpBitClient upBitClient;
    private static final String PATH = "/ticker";
    private static final String QUERY_NAME = "markets";

    public List<TickerResponse> getTicker(List<UpBitCoinType> upBitCoinTypes) {
        List<String> types = upBitCoinTypes.stream()
                .map(UpBitCoinType::getName)
                .collect(Collectors.toList());

        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(PATH)
                .queryParam(QUERY_NAME, types)
                .build();

        return List.of(upBitClient.get(uri.toString(), TickerResponse[].class));
    }

    public List<TickerResponse> getCommonTicker() {
        List<String> types = Arrays.stream(CommonCoinType.values())
                .map(i -> "KRW-" + i.toString())
                .collect(Collectors.toList());

        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(PATH)
                .queryParam(QUERY_NAME, types)
                .build();

        return List.of(upBitClient.get(uri.toString(), TickerResponse[].class));
    }

}

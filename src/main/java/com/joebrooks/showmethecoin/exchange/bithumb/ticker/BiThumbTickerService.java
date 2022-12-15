package com.joebrooks.showmethecoin.exchange.bithumb.ticker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.showmethecoin.exchange.CommonCoinType;
import com.joebrooks.showmethecoin.exchange.bithumb.client.BiThumbClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BiThumbTickerService {

	private final BiThumbClient biThumbClient;
	private static final String PATH = "/ticker/ALL_KRW";
	private final ObjectMapper mapper;

	public List<TickerResponse> getTickers() {
		UriComponents uri = UriComponentsBuilder.newInstance()
			.path(PATH)
			.build();

		Map<String, String> result = (Map)biThumbClient.get(uri.toString(), HashMap.class).get("data");
		List<TickerResponse> responses = new ArrayList<>();

		for (String key : result.keySet()) {
			if (Arrays.stream(CommonCoinType.values()).anyMatch(i -> i.toString().equals(key))) {
				TickerResponse response = mapper.convertValue(result.get(key), TickerResponse.class);
				response.setMarket(key);
				
				responses.add(response);
			}
		}

		return responses;
	}

}

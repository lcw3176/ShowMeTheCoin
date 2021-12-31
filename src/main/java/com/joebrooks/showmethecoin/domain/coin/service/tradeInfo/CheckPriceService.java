package com.joebrooks.showmethecoin.domain.coin.service.tradeInfo;

import com.joebrooks.showmethecoin.domain.coin.model.tradeInfo.TradeInfoQuery;
import com.joebrooks.showmethecoin.domain.coin.model.tradeInfo.TradeInfoUri;
import com.joebrooks.showmethecoin.infra.common.HeaderGenerator;
import com.joebrooks.showmethecoin.infra.model.ApiUri;
import com.joebrooks.showmethecoin.util.RequestUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CheckPriceService {

    public float getPrice(String coinName) throws ParseException {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme(ApiUri.scheme.toString())
                .host(ApiUri.host.toString())
                .path(TradeInfoUri.RequestCoinInfo.toString())
                .queryParam(TradeInfoQuery.RequestCoinInfo.toString(), coinName)
                .build(true);

        ResponseEntity<String> responseEntity = RequestUtil.sendGet(uri);

        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray)jsonParser.parse(responseEntity.getBody());
        String price = ((JSONObject)jsonArray.get(0)).get("trade_price").toString();

        return Float.parseFloat(price);
    }

    public static ResponseEntity<String> sendGet(UriComponents uri){
        HttpEntity<String> entity = new HttpEntity<String>("", HeaderGenerator.getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);
    }

}

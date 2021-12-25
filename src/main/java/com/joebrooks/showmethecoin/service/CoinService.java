package com.joebrooks.showmethecoin.service;

import com.joebrooks.showmethecoin.util.RequestUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Service
public class CoinService {

    // return uuid
    public String buy(String coinName, float volume, float price) throws NoSuchAlgorithmException, UnsupportedEncodingException, ParseException {
        HashMap<String, String> params = new HashMap<>();
        params.put("market", coinName);
        params.put("side", "bid");
        params.put("volume", Float.toString(volume));
        params.put("price", Float.toString(price));
        params.put("ord_type", "limit");

        ResponseEntity<String> entity = RequestUtil.sendOrder(params);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(entity.getBody());

        return jsonObject.get("uuid").toString();
    }

    public void sell(String coinName, float volume, float price) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        HashMap<String, String> params = new HashMap<>();
        params.put("market", coinName);
        params.put("side", "ask");
        params.put("volume", Float.toString(volume));
        params.put("price", Float.toString(price));
        params.put("ord_type", "limit");

        RequestUtil.sendOrder(params);
    }


    public float getPrice(String coinName) throws ParseException {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.upbit.com")
                .path("/v1/ticker")
                .queryParam("markets", coinName)
                .build(true);

        ResponseEntity<String> responseEntity = RequestUtil.sendGet(uri);

        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray)jsonParser.parse(responseEntity.getBody());
        String price = ((JSONObject)jsonArray.get(0)).get("trade_price").toString();

        return Float.parseFloat(price);
    }



}

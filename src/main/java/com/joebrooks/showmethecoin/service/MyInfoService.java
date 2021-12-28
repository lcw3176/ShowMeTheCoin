package com.joebrooks.showmethecoin.service;

import com.joebrooks.showmethecoin.util.OrderParseUtil;
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

@Service
public class MyInfoService {


    public float getMoneyBalance() throws ParseException {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.upbit.com")
                .path("/v1/accounts")
                .build(true);

        ResponseEntity<String> responseEntity = RequestUtil.sendGet(uri);

        return OrderParseUtil.getBalance(responseEntity, "KRW");
    }

    public float getCoinBalance(String coinName) throws ParseException {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.upbit.com")
                .path("/v1/accounts")
                .build(true);

        ResponseEntity<String> responseEntity = RequestUtil.sendGet(uri);

        return OrderParseUtil.getBalance(responseEntity, coinName.split("-")[1]);
    }

    public ResponseEntity<String> getOrderInfo(String uuid) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return RequestUtil.getOrderInfo(uuid);
    }

    public boolean cancelOrder(String uuid) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return RequestUtil.cancelOrder(uuid);
    }

}

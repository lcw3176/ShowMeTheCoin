package com.joebrooks.showmethecoin.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestUtil {


    private static String getQueryHash(HashMap<String, String> params) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        ArrayList<String> queryElements = new ArrayList<>();

        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        return queryHash;
    }


    private static String getQueryHash(String uuid) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        HashMap<String, String> params = new HashMap<>();
        params.put("uuid", uuid);

        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        return queryHash;
    }


    public static ResponseEntity<String> sendGet(UriComponents uri){
        HttpEntity<String> entity = new HttpEntity<String>("", getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);
    }

    public static ResponseEntity<String> sendOrder(HashMap<String, String> params) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String queryHash = getQueryHash(params);

        HttpEntity<HashMap<String, String>> entity = new HttpEntity<>(params, getHeaders(queryHash));
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.upbit.com")
                .path("/v1/orders")
                .build(true);

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(uri.toString(), HttpMethod.POST, entity, String.class);
    }

    public static ResponseEntity<String> getOrderInfo(String uuid) throws NoSuchAlgorithmException, UnsupportedEncodingException {


        HttpEntity<String> entity = new HttpEntity<String>("", getHeaders(queryHash));
        RestTemplate restTemplate = new RestTemplate();

        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.upbit.com")
                .path("/v1/order")
                .queryParam("uuid", uuid)
                .build(true);

        return restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);
    }

    public static boolean cancelOrder(String uuid) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        HashMap<String, String> params = new HashMap<>();
        params.put("uuid", uuid);

        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        HttpEntity<String> entity = new HttpEntity<String>("", getHeaders(queryHash));
        RestTemplate restTemplate = new RestTemplate();

        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.upbit.com")
                .path("/v1/order")
                .queryParam("uuid", uuid)
                .build(true);

        return restTemplate.exchange(uri.toString(), HttpMethod.DELETE, entity, String.class).getStatusCode().is2xxSuccessful();
    }
}

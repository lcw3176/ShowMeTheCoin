package com.joebrooks.showmethecoin.domain.coin.service.order;

import com.joebrooks.showmethecoin.domain.coin.model.order.OrderType;
import com.joebrooks.showmethecoin.domain.coin.model.order.OrderUri;
import com.joebrooks.showmethecoin.infra.common.HeaderGenerator;
import com.joebrooks.showmethecoin.infra.model.ApiUri;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderRequestService {

    private String getQueryHash(HashMap<String, String> params) throws NoSuchAlgorithmException {
        ArrayList<String> queryElements = new ArrayList<>();

        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes(StandardCharsets.UTF_8));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        return queryHash;
    }


    private String getOrderUri(OrderType orderType, String uuid){
        UriComponents uri;

        if(orderType.equals(OrderType.RequestOrder)){
            uri = UriComponentsBuilder.newInstance()
                    .scheme(ApiUri.scheme.toString())
                    .host(ApiUri.host.toString())
                    .path(OrderUri.RequestOrder.toString())
                    .build(true);
        } else {
            uri = UriComponentsBuilder.newInstance()
                    .scheme(ApiUri.scheme.toString())
                    .host(ApiUri.host.toString())
                    .path(OrderUri.CancelOrder.toString())
                    .queryParam("uuid", uuid)
                    .build(true);
        }


        return uri.toUriString();
    }


    public ResponseEntity<String> requestOrder(HashMap<String, String> params) throws NoSuchAlgorithmException {
        HttpEntity<HashMap<String, String>> entity = new HttpEntity<>(params,  HeaderGenerator.getHeaders(getQueryHash(params)));

        return  new RestTemplate().exchange(
                getOrderUri(OrderType.RequestOrder, null),
                HttpMethod.POST,
                entity,
                String.class);
    }

    public boolean cancelOrder(String uuid) throws NoSuchAlgorithmException {
        HashMap<String, String> map = new HashMap<>();
        map.put("uuid", uuid);

        HttpEntity<String> entity = new HttpEntity<>("",  HeaderGenerator.getHeaders(getQueryHash(map)));

        return new RestTemplate().exchange(
                getOrderUri(OrderType.CancelOrder, uuid),
                HttpMethod.DELETE,
                entity,
                String.class).getStatusCode().is2xxSuccessful();
    }
}

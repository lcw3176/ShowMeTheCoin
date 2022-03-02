package com.joebrooks.showmethecoin.global.upbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.showmethecoin.order.OrderRequest;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class QueryGenerator {
    private final ObjectMapper mapper = new ObjectMapper();

    public String convertQueryString(HashMap<String, String> map) throws NoSuchAlgorithmException {
        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : map.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());

        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes(StandardCharsets.UTF_8));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));


        return queryHash;
    }


    public HashMap<String, String> getQueryMap(OrderRequest orderRequest){
        HashMap<String, String> map = mapper.convertValue(orderRequest, HashMap.class);

        if(orderRequest.getSide().equals(Side.bid)){
            map.remove("volume");
        } else {
            map.remove("price");
        }

        return map;
    }
}

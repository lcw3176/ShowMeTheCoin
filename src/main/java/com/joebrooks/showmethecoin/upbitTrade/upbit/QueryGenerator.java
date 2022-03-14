package com.joebrooks.showmethecoin.upbitTrade.upbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class QueryGenerator {
    private final ObjectMapper mapper = new ObjectMapper();

    public String convertQueryToHash(HashMap<String, String> map) throws NoSuchAlgorithmException {
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


    public HashMap<String, String> getQueryMap(Object request){
        HashMap<String, String> map = mapper.convertValue(request, HashMap.class);

        return map;
    }
}

package com.joebrooks.showmethecoin.autotrade.upbit.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
@Slf4j
public class QueryGenerator {
    private final ObjectMapper mapper = new ObjectMapper();

    public String convertQueryToHash(HashMap<String, String> map) {
        try{
            ArrayList<String> queryElements = new ArrayList<>();
            for(Map.Entry<String, String> entity : map.entrySet()) {
                queryElements.add(entity.getKey() + "=" + entity.getValue());

            }

            String queryString = String.join("&", queryElements.toArray(new String[0]));

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(queryString.getBytes(StandardCharsets.UTF_8));

            return String.format("%0128x", new BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);

            throw new RuntimeException(e.getMessage(), e);
        }

    }


    public HashMap<String, String> getQueryMap(Object request){
        return mapper.convertValue(request, HashMap.class);
    }
}

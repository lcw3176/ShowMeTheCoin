package com.joebrooks.showmethecoin.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class OrderParseUtil {

    public static boolean isOrderComplete(ResponseEntity<String> responseEntity) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(responseEntity.getBody());

        return jsonObject.get("state").toString().equals("done");
    }

    public static float getAvailableBalance(ResponseEntity<String> responseEntity, String coinName) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray)jsonParser.parse(responseEntity.getBody());
        String price = null;

        for(Object i : jsonArray){

            if(((JSONObject)i).get("currency").equals(coinName)){
                price = ((JSONObject)i).get("balance").toString();
            }
        }

        return Float.parseFloat(Optional.ofNullable(price).orElse("0.0"));
    }


    public static float getLockedBalance(ResponseEntity<String> responseEntity, String coinName) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray)jsonParser.parse(responseEntity.getBody());
        String price = null;

        for(Object i : jsonArray){

            if(((JSONObject)i).get("currency").equals(coinName)){
                price = ((JSONObject)i).get("locked").toString();
            }
        }

        return Float.parseFloat(Optional.ofNullable(price).orElse("0.0"));
    }

    public static float getPriceWhenTraded(ResponseEntity<String> responseEntity) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(responseEntity.getBody());

        return Float.parseFloat(jsonObject.get("price").toString());
    }

    public static float getVolumeWhenTraded(ResponseEntity<String> responseEntity) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(responseEntity.getBody());

        return Float.parseFloat(jsonObject.get("volume").toString());
    }

    public static boolean wasBought(ResponseEntity<String> responseEntity) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(responseEntity.getBody());

        return jsonObject.get("side").toString().equals("bid");
    }
}

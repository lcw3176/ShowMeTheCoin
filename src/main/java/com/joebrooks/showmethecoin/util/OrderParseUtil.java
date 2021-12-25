package com.joebrooks.showmethecoin.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;

public class OrderParseUtil {

    public static boolean isOrderComplete(ResponseEntity<String> responseEntity) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(responseEntity.getBody());

        return jsonObject.get("state").toString().equals("done");
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

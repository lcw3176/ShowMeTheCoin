package com.joebrooks.showmethecoin.domain.global.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;

public class JsonResponseParser {

    public static String getProperty(ResponseEntity<String> entity, String propertyName, boolean isArray) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(entity.getBody());

        return jsonObject.get(propertyName).toString();
    }


}

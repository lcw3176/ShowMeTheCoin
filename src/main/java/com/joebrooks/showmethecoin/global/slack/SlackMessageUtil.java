package com.joebrooks.showmethecoin.global.slack;

import lombok.experimental.UtilityClass;
import net.minidev.json.JSONObject;
import org.json.simple.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@UtilityClass
public class SlackMessageUtil {

    private JSONObject getHeader(String title){
        JSONObject block = new JSONObject();
        block.put("type", "plain_text");
        block.put("text", title);

        JSONObject header = new JSONObject();
        header.put("type", "header");
        header.put("text", block);

        return header;
    }

    private JSONObject getSection(Map<String, Object> map){

        JSONObject textJson = new JSONObject();

        map.forEach((key, value) -> {
            textJson.put("type", "mrkdwn");
            textJson.put("text", key + ": " + value);
        });

        JSONObject json = new JSONObject();
        json.put("type", "section");
        json.put("text", textJson);

        return json;
    }

    private JSONObject getBlock(JSONObject ... jsons){
        JSONArray arr = new JSONArray();

        for(var i : jsons){
            arr.add(i);
        }

        JSONObject json = new JSONObject();
        json.put("blocks", arr);

        return json;
    }

    public String makeErrorMessage() {
        JSONObject headerJson = getHeader("Error");
        Map<String, Object> map = new HashMap<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd a HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        map.put("시간", simpleDateFormat.format(new Date()));

        JSONObject sectionOneJson = getSection(map);
        map.clear();

        map.put("코인 매매", "중단됨");
        JSONObject sectionTwoJson = getSection(map);
        map.clear();

        return getBlock(headerJson, sectionOneJson, sectionTwoJson).toJSONString();
    }
}
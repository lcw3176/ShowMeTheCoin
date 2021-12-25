package com.joebrooks.showmethecoin.util;

import com.joebrooks.showmethecoin.domain.CoinTradeInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MessageUtil {

    private static JSONObject getHeader(String title){
        JSONObject block = new JSONObject();
        block.put("type", "plain_text");
        block.put("text", title);

        JSONObject header = new JSONObject();
        header.put("type", "header");
        header.put("text", block);

        return header;
    }

    private static JSONObject getSection(Map<String, Object> map){
        JSONArray jArr = new JSONArray();

        map.forEach((key, value) -> {
            JSONObject json = new JSONObject();
            json.put("type", "mrkdwn");
            json.put("text", key + ": " + value);

            jArr.add(json);
        });

        JSONObject json = new JSONObject();
        json.put("fields", jArr);
        json.put("type", "section");

        return json;
    }

    private static JSONObject getBlock(JSONObject ... jsons){
        JSONArray arr = new JSONArray();

        for(var i : jsons){
            arr.add(i);
        }

        JSONObject json = new JSONObject();
        json.put("blocks", arr);

        return json;
    }

    public static String makeBuyMessage(CoinTradeInfo coinInfo){
        JSONObject headerJson = getHeader("매수");
        Map<String, Object> map = new HashMap<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd a HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        map.put("시간", simpleDateFormat.format(new Date()));
        map.put("잔고", coinInfo.getMyCash());

        JSONObject sectionOneJson = getSection(map);
        map.clear();

        map.put("매수량", coinInfo.getVolume());
        map.put("매수 가격", coinInfo.getPrice());

        JSONObject sectionTwoJson = getSection(map);

        return getBlock(headerJson, sectionOneJson, sectionTwoJson).toJSONString();
    }

    public static String makeSellMessage(CoinTradeInfo coinInfo){
        JSONObject headerJson = getHeader("매도");
        Map<String, Object> map = new HashMap<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd a HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        map.put("시간", simpleDateFormat.format(new Date()));

        JSONObject sectionOneJson = getSection(map);
        map.clear();

        map.put("매도량", coinInfo.getVolume());
        map.put("매도 가격", coinInfo.getPrice());

        JSONObject sectionTwoJson = getSection(map);

        return getBlock(headerJson, sectionOneJson, sectionTwoJson).toJSONString();
    }

    public static String makeDailyMessage(float benefit, float sellingMoney, float buyingMoney){
        JSONObject headerJson = getHeader("Daily Report");
        Map<String, Object> map = new HashMap<>();
        map.put("수익", benefit);

        JSONObject sectionOneJson = getSection(map);

        map.clear();
        map.put("판매량", sellingMoney);
        map.put("구매량", buyingMoney);


        JSONObject sectionTwoJson = getSection(map);

        return getBlock(headerJson, sectionOneJson, sectionTwoJson).toJSONString();
    }
}

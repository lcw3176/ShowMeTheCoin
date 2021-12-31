package com.joebrooks.showmethecoin.domain.coin.service.order;

import com.joebrooks.showmethecoin.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRequestService orderRequestService;

    /// return uuid
    public String buy(String coinName, float volume, float price) throws NoSuchAlgorithmException, ParseException {
        HashMap<String, String> params = new HashMap<>();
        params.put("market", coinName);
        params.put("side", "bid");
        params.put("volume", Float.toString(volume));
        params.put("price", Float.toString(price));
        params.put("ord_type", "limit");


        ResponseEntity<String> entity = orderRequestService.requestOrder(params);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(entity.getBody());

        return jsonObject.get("uuid").toString();
    }

    public void sell(){

    }
}

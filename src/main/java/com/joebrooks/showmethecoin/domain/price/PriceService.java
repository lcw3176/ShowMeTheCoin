package com.joebrooks.showmethecoin.domain.price;

import com.joebrooks.showmethecoin.global.client.ClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final ClientRequest clientRequest;


    public Optional<List<Price>> getPrices(Day day){
        int count = day.ordinal() + 1;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Price> val = null;

        for(int i = 0; i < count; i++) {
            String strDate = LocalDateTime.now(ZoneOffset.UTC).minusDays(i).format(format);
            val = clientRequest
                    .getAndReceiveList("candles/minutes/10?market=KRW-BTC&count=144&to=" + strDate, Price.class);
        }


        return Optional.of(val);
    }

    private void getUps(){

    }

    private void getDowns(){

    }

}

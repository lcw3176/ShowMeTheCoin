package com.joebrooks.showmethecoin.domain.strategy.strategyType;

import ch.qos.logback.core.util.FixedDelay;
import com.joebrooks.showmethecoin.global.candles.Candle;
import com.joebrooks.showmethecoin.global.candles.CandleService;
import com.joebrooks.showmethecoin.global.client.ClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Component("rsi")
@RequiredArgsConstructor
public class Rsi implements IStrategy {

    private final CandleService candleService;
    private final List<Candle> val = new LinkedList<>();

    @Scheduled(fixedDelay = 1000)
    public void data(){
        val.add(candleService.getCandleData());
    }

    private HashMap<String, Double> getRsi(){
        int days = 14;

        if(val.size() < 60){
            return null;
        }

        List<Double> ups = new LinkedList<>();
        List<Double> downs = new LinkedList<>();
        List<String> time = new LinkedList<>();
        List<Double> rsi = new LinkedList<>();
        List<Double> price = new LinkedList<>();

        HashMap<String, Double> timingSheet = new HashMap<>();

        for(int i = 0; i < val.size() - days; i++){
            double tempUp = 0;
            double tempDown = 0;

            for(int j = 0; j < days; j++){
                double diff = Double.parseDouble(val.get(i + j).getTradePrice().toString())
                        - Double.parseDouble(val.get(i + (j + 1)).getTradePrice().toString());

                if(diff > 0){
                    tempUp += diff;
                } else {
                    tempDown += Math.abs(diff);
                }
            }

            time.add(val.get(i).getDateKst());
            price.add(val.get(i).getTradePrice());

            if(i == 0){
                ups.add(tempUp / days);
                downs.add(tempDown / days);
            } else {
                ups.add(((days - 1) * ups.get(i - 1) + tempUp) / days);
                downs.add(((days - 1) * downs.get(i - 1) + tempDown) / days);
            }
        }

        for(int i = 0; i < ups.size(); i++){
            double rs = ups.get(i) / downs.get(i);
            rsi.add(rs / (1 + rs));
        }

        double beforeRsi = 0;
        boolean fallingFlag = false;
        int count = 0;

        for(int i = 0; i < rsi.size(); i++){

            if(i != 0){
                double temp = rsi.get(i) - beforeRsi;

                if(temp < 0){
                    fallingFlag = true;
                } else {
                    fallingFlag = false;
                }
            }

            if(fallingFlag) {
                count++;
                if(count >= 10){
                    timingSheet.put(time.get(i) + "매수  " + price.get(i), rsi.get(i));
                    count = 0;
                }
            } else {
                count = 0;
            }

            beforeRsi = rsi.get(i);
        }

        return timingSheet;
    }

    @Override
    public HashMap<String, Double> execute() {


        return getRsi();
    }
}

//package com.joebrooks.showmethecoin.upbit.indicator;
//
//import com.joebrooks.showmethecoin.upbit.candles.CandleResponse;
//import com.joebrooks.showmethecoin.global.graph.GraphUtil;
//import com.joebrooks.showmethecoin.upbit.indicator.IIndicator;
//import com.joebrooks.showmethecoin.upbit.indicator.IndicatorResponse;
//import com.joebrooks.showmethecoin.upbit.indicator.type.IndicatorAnnotation;
//import com.joebrooks.showmethecoin.upbit.indicator.type.IndicatorType;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//
//@Component(IndicatorAnnotation.RMI)
//public class Rmi implements IIndicator {
//
//    @Value("${upbit.rmi.day}")
//    private int day;
//
//    @Value("${upbit.rmi.n}")
//    private int n;
//
//    public List<Double> getRmi(List<CandleResponse> data){
//        List<CandleResponse> copyData = new LinkedList<>(data);
//
//        Collections.copy(copyData, data);
//        Collections.reverse(copyData);
//
//        List<Double> rmiLst = new LinkedList<>();
//        List<Double> ups = new LinkedList<>();
//        List<Double> downs = new LinkedList<>();
//
//        List<Double> mu = new LinkedList<>();
//        List<Double> md = new LinkedList<>();
//
//        for(int i = n; i < copyData.size(); i++){
//            double diff = copyData.get(i).getTradePrice() - copyData.get(i - n).getTradePrice();
//
//            if(diff > 0){
//                ups.add(diff);
//                downs.add(0D);
//            } else {
//                downs.add(Math.abs(diff));
//                ups.add(0D);
//            }
//        }
//
//        for(int i = day; i < ups.size() + 1; i++){
//            double tempUp = 0D;
//            double tempDown = 0D;
//
//            for(int j = i - day; j < i; j++){
//                tempUp += ups.get(j);
//                tempDown += downs.get(j);
//            }
//
//            mu.add(tempUp / day);
//            md.add(tempDown / day);
//        }
//
//
//        for(int i = mu.size() - 1; i >= 0; i--){
//            double rm = mu.get(i) / md.get(i);
//            double rmi = rm / (1 + rm);
//
//            rmiLst.add(rmi * 100);
//        }
//
//        return rmiLst;
//    }
//
//
//    @Override
//    public IndicatorResponse execute(List<CandleResponse> candles) {
//        List<Double> rmi = getRmi(candles);
//
//        return IndicatorResponse.builder()
//                .type(IndicatorType.RMI)
//                .value(rmi.get(0))
//                .status(GraphUtil.getStatus(rmi.get(1), rmi.get(0)))
//                .build();
//    }
//}

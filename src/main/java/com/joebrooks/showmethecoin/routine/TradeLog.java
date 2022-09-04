package com.joebrooks.showmethecoin.routine;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TradeLog{
    private UserEntity userId;
    private List<Double> sellPrice;
    private List<Double> buyPrice;
    private LocalDateTime lastOrder;
    private double executeVolume;


    public void changeLastOrderTime(LocalDateTime time){
        this.lastOrder = time;
    }
}

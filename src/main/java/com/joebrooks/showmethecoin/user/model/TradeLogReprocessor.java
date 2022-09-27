package com.joebrooks.showmethecoin.user.model;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TradeLogReprocessor {
    private UserEntity userId;
    private List<Double> sellPrice;
    private List<Double> buyPrice;
    private LocalDateTime lastOrder;
    private BigDecimal executeVolume;


    public void changeLastOrderTime(LocalDateTime time){
        this.lastOrder = time;
    }
}

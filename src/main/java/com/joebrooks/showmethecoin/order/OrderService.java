package com.joebrooks.showmethecoin.order;

import com.joebrooks.showmethecoin.global.upbit.UpBitClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UpBitClient upBitClient;
    private final String path = "/orders";

    public OrderResponse requestOrder(OrderRequest request){
        return upBitClient.requestOrder(request, path);
    }

    public List<CheckOrderResponse> checkOrder(CheckOrderRequest request){
        return new LinkedList<>(Arrays.asList(upBitClient.checkOrder(request, path)));
    }

}

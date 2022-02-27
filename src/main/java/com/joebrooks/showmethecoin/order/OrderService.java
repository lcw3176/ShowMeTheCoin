package com.joebrooks.showmethecoin.order;

import com.joebrooks.showmethecoin.common.upbit.UpBitClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UpBitClient upBitClient;
    private final String path = "/orders";

    public OrderResponse requestOrder(OrderRequest request){
        return upBitClient.requestOrder(request, path);
    }

}

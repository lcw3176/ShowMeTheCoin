package com.joebrooks.showmethecoin.autotrade.upbit.order;

import com.joebrooks.showmethecoin.autotrade.upbit.client.UpBitClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UpBitClient upBitClient;

    public OrderResponse requestOrder(OrderRequest request){
        String path = "/orders";

        return upBitClient.post(path, request, OrderResponse.class);
    }

    public List<CheckOrderResponse> checkOrder(CheckOrderRequest request){
        String path = "/orders";

        UriComponents uri = UriComponentsBuilder.newInstance()
                                .path(path)
                                .queryParam("state", request.getState())
                                .build();

        return Arrays.asList(upBitClient.get(uri.toString(), true, request, CheckOrderResponse[].class));
    }

    public void cancelOrder(CancelOrderRequest request){
        String path = "/order";

        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(path)
                .queryParam("uuid", request.getUuid())
                .build();

        upBitClient.delete(uri.toString(), request);
    }

}

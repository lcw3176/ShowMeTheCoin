package com.joebrooks.showmethecoin.upbit.order;

import com.joebrooks.showmethecoin.upbit.client.UpBitClient;
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
    private final String path = "/orders";

    public OrderResponse requestOrder(OrderRequest request){
        return upBitClient.post(path, request, OrderResponse.class);
    }

    public List<CheckOrderResponse> checkOrder(CheckOrderRequest request){
        UriComponents uri = UriComponentsBuilder.newInstance()
                                .path(path)
                                .queryParam("state", request.getState())
                                .build();

        return Arrays.asList(upBitClient.get(uri.toString(), request, CheckOrderResponse[].class));
    }

}

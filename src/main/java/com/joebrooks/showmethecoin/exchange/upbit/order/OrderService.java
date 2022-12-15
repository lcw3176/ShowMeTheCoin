package com.joebrooks.showmethecoin.exchange.upbit.order;

import com.joebrooks.showmethecoin.exchange.upbit.client.UpBitClient;
import com.joebrooks.showmethecoin.exchange.upbit.order.model.CancelOrderRequest;
import com.joebrooks.showmethecoin.exchange.upbit.order.model.CheckOrderRequest;
import com.joebrooks.showmethecoin.exchange.upbit.order.model.CheckOrderResponse;
import com.joebrooks.showmethecoin.exchange.upbit.order.model.OrderRequest;
import com.joebrooks.showmethecoin.exchange.upbit.order.model.OrderResponse;
import com.joebrooks.showmethecoin.repository.userkey.UserKeyEntity;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UpBitClient upBitClient;

    public OrderResponse requestOrder(OrderRequest request, UserKeyEntity userKey) {
        String path = "/orders";

        return upBitClient.post(path, request, userKey, OrderResponse.class);
    }

    public List<CheckOrderResponse> checkOrder(CheckOrderRequest request, UserKeyEntity userKey) {
        String path = "/orders";

        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(path)
                .queryParam("state", request.getState())
                .build();

        return Arrays.asList(upBitClient.get(uri.toString(), userKey, request, CheckOrderResponse[].class));
    }

    public void cancelOrder(CancelOrderRequest request, UserKeyEntity userKey) {
        String path = "/order";

        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(path)
                .queryParam("uuid", request.getUuid())
                .build();

        upBitClient.delete(uri.toString(), userKey, request);
    }

    public boolean isEveryOrderDone(UserKeyEntity userKey) {
        boolean waitDone = checkOrder(CheckOrderRequest.builder()
                .state(OrderStatus.wait)
                .build(), userKey).isEmpty();

        boolean watchDone = checkOrder(CheckOrderRequest.builder()
                .state(OrderStatus.watch)
                .build(), userKey).isEmpty();

        return waitDone && watchDone;
    }

}

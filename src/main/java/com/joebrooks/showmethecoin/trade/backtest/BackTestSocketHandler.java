package com.joebrooks.showmethecoin.trade.backtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class BackTestSocketHandler extends TextWebSocketHandler {

    private final BackTestService backTestService;
    private final ObjectMapper mapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        BackTestRequest backTestRequest = mapper.readValue(message.getPayload(), BackTestRequest.class);
        backTestService.start(backTestRequest, session);
    }


}
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

    private final BackTestExecutor backTestExecutor;
    private final ObjectMapper mapper;
    private final BackTestService backTestService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        BackTestRequest backTestRequest = mapper.readValue(message.getPayload(), BackTestRequest.class);
//        backTestExecutor.execute(backTestRequest, session);
        backTestService.start(backTestRequest, session);
    }


}
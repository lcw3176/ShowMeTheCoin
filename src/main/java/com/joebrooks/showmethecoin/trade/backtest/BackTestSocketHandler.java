package com.joebrooks.showmethecoin.trade.backtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BackTestSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessionList = new LinkedList<>();
    private final BackTestService backTestService;
    private final ObjectMapper mapper;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionList.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        BackTestRequest backTestRequest = mapper.readValue(message.getPayload(), BackTestRequest.class);
        backTestService.start(backTestRequest, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionList.remove(session);
    }



    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        sessionList.remove(session);
    }


}
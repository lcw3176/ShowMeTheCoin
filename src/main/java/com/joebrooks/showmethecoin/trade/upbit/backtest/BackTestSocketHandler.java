package com.joebrooks.showmethecoin.trade.upbit.backtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joebrooks.showmethecoin.global.exception.type.AutomationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BackTestSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessionList = new LinkedList<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final BackTestService backTestService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionList.add(session);
    }

    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        BackTestRequest backTestRequest = mapper.readValue(message.getPayload(), BackTestRequest.class);
        backTestService.start(backTestRequest);
    }


    @EventListener
    public void sendData(BackTestResponse response) throws IOException {
        sessionList.get(0).sendMessage(new TextMessage(mapper.writeValueAsString(response)));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionList.remove(session);
        backTestService.stop();
    }

}
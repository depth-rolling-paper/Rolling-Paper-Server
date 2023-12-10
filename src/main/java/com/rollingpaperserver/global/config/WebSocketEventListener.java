package com.rollingpaperserver.global.config;

import com.rollingpaperserver.domain.waitingRoom.application.WaitingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RequiredArgsConstructor
@Component
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;
//    private final WaitingRoomService waitingRoomService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        // Description : WebSocket 연결 시 실행되는 로직
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // Description : WebSocket 연결 해제 시 실행되는 로직
    }

    public void sendUserCountUpdate(String waitingRoomUrl, int userCount) {
        // Description : 유저 수 업데이트, 업데이트 된 유저 수 보내기
        messagingTemplate.convertAndSend("/topic/" + waitingRoomUrl, userCount);
    }
}
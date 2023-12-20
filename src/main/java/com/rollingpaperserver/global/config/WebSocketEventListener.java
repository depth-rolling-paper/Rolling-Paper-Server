package com.rollingpaperserver.global.config;

import com.rollingpaperserver.domain.waitingRoom.application.WaitingRoomService;
import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import com.rollingpaperserver.domain.waitingRoom.domain.repository.WaitingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final WaitingRoomRepository waitingRoomRepository;

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

    public void sendUrl(String roomUrl) {
        messagingTemplate.convertAndSend("/topic/" + roomUrl, roomUrl);
    }

    // Description : 시간이 되었을 때 입장 가능토록 하기 위함
    public void sendCanStart(int minutes, String waitingRoomUrl) {
        scheduler.schedule(() -> {
            Optional<WaitingRoom> byUrl = waitingRoomRepository.findByUrl(waitingRoomUrl);

            // If : 비어있는 경우 (이미 시작된 경우)
            if (byUrl.isEmpty()) {
                return;
            }

            WaitingRoom waitingRoom = byUrl.get();

            // If : 꽉 차있으면 굳이 보낼 필요 없음
            if (waitingRoom.getCurrent_user_num() == waitingRoom.getLimit_user_num()) {
                return;
            }

            // Scheduled time reached, send message to the client
            messagingTemplate.convertAndSend("/topic/" + waitingRoomUrl, true);
        }, minutes, TimeUnit.MINUTES);
    }

}
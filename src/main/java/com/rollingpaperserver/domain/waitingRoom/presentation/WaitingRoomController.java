package com.rollingpaperserver.domain.waitingRoom.presentation;

import com.rollingpaperserver.domain.waitingRoom.application.WaitingRoomService;
import com.rollingpaperserver.domain.waitingRoom.dto.resquest.CreateWaitingRoomReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/waiting-rooms")
public class WaitingRoomController {

    private final WaitingRoomService waitingRoomService;

    // Description : 대기 방 생성
    // TODO : OK
    @PostMapping
    public ResponseEntity<?> createWaitingRoom(@RequestBody CreateWaitingRoomReq createWaitingRoomReq) {
        return waitingRoomService.createWaitingRoom(createWaitingRoomReq);

    }

    // Description : url과 일치하는 대기 방 존재 여부 - 유효성 검사
    // TODO : URL 생김새에 대해 논의필요
    @GetMapping("/exist/{url}")
    public ResponseEntity<?> findWaitingRoom(@PathVariable(value = "url") String url) {
        return waitingRoomService.findWaitingRoom(url);
    }

    // Description : 현재 대기 인원 조회 - // 참여할 수 있는지 확인해봐야 하므로 ??
    // TODO : OK
    @GetMapping("/{waiting-room-id}")
    public ResponseEntity<?> checkCanJoinWaitingRoom(@PathVariable(value = "waiting-room-id") Long waitingRoomId) {
        return waitingRoomService.checkCanJoinWaitingRoom(waitingRoomId);
    }
}

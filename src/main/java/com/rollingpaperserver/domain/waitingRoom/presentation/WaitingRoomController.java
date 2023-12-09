package com.rollingpaperserver.domain.waitingRoom.presentation;

import com.rollingpaperserver.domain.waitingRoom.application.WaitingRoomService;
import com.rollingpaperserver.domain.waitingRoom.dto.resquest.CreateWaitingRoomReq;
import com.rollingpaperserver.domain.waitingRoom.dto.resquest.FindWaitingRoomUrlReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/waiting-rooms")
public class WaitingRoomController {

    private final WaitingRoomService waitingRoomService;

    /**
     * Return type에 대해 Response DTO를 만들어 반환하는 것 생각
     * 
     */
    
    // Description : 대기 방 생성
    @PostMapping
    public ResponseEntity<?> createWaitingRoom(@RequestBody CreateWaitingRoomReq createWaitingRoomReq) {
        return waitingRoomService.createWaitingRoom(createWaitingRoomReq);

    }

    // Description : url과 일치하는 대기 방 존재 여부
    @GetMapping("/exist/{url}")
//    public ResponseEntity<?> findWaitingRoom(@RequestBody FindWaitingRoomUrlReq findWaitingRoomUrlReq) {
    public ResponseEntity<?> findWaitingRoom(@PathVariable(value = "url") String url) {
        return waitingRoomService.findWaitingRoom(url);
    }

    // Description : 현재 대기 인원 조회 - // 참여할 수 있는지 확인해봐야 하므로 ??
    @GetMapping("/{waiting-room-id}")
    public ResponseEntity<?> checkCanJoinWaitingRoom(@PathVariable(value = "waiting-room-id") Long waitingRoomId) {
        return waitingRoomService.checkCanJoinWaitingRoom(waitingRoomId);
    }
}

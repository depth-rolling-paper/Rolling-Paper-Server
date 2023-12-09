package com.rollingpaperserver.domain.room.presentation;

import com.rollingpaperserver.domain.room.application.RoomService;
import com.rollingpaperserver.domain.room.dto.request.CreateRoomReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    // TODO : 필요 시 해당 방 유저 목록 조회 및 롤링페이퍼 관련 정보 API 추가

    // Description : 방 생성
    // TODO : OK
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomReq createRoomReq) {
        return roomService.createRoom(createRoomReq);

    }

}

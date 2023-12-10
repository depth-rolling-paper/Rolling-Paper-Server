package com.rollingpaperserver.domain.room.presentation;

import com.rollingpaperserver.domain.room.application.RoomService;
import com.rollingpaperserver.domain.room.dto.request.CreateRoomReq;
import com.rollingpaperserver.domain.room.dto.response.CreateRoomRes;
import com.rollingpaperserver.domain.user.dto.response.FindUserRes;
import com.rollingpaperserver.domain.waitingRoom.dto.response.FindWaitingRoomRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Room API", description = "Room 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    // TODO : 필요 시 해당 방 유저 목록 조회 및 롤링페이퍼 관련 정보 API 추가

    // Description : 방 생성
    // TODO : OK
    @Operation(summary = "롤링페이퍼 작성 방 생성", description = "롤링페이퍼 작성 방을 생성합니다. 대기 방의 유저가 방으로 이동됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "방 생성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateRoomRes.class))}),
            @ApiResponse(responseCode = "400", description = "방 생성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindWaitingRoomRes.class))}),
    })
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomReq createRoomReq) {
        return roomService.createRoom(createRoomReq);

    }

}

package com.rollingpaperserver.domain.waitingRoom.presentation;

import com.rollingpaperserver.domain.waitingRoom.application.WaitingRoomService;
import com.rollingpaperserver.domain.waitingRoom.dto.response.CreateWaitingRoomRes;
import com.rollingpaperserver.domain.waitingRoom.dto.response.FindWaitingRoomRes;
import com.rollingpaperserver.domain.waitingRoom.dto.response.FindWaitingRoomWithCountRes;
import com.rollingpaperserver.domain.waitingRoom.dto.response.JoinWaitingRoomRes;
import com.rollingpaperserver.domain.waitingRoom.dto.resquest.CreateWaitingRoomReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WaitingRoom API", description = "WaitingRoom 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/waiting-rooms")
public class WaitingRoomController {

    private final WaitingRoomService waitingRoomService;

    // Description : 대기 방 생성
    // TODO : OK
    @Operation(summary = "대기 방 생성", description = "대기 방 생성을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대기 방 생성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateWaitingRoomRes.class))}),
            @ApiResponse(responseCode = "400", description = "대기 방 생성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseEntity<?> createWaitingRoom(@RequestBody CreateWaitingRoomReq createWaitingRoomReq) {
        return waitingRoomService.createWaitingRoom(createWaitingRoomReq);

    }

    // Description : url과 일치하는 대기 방 존재 여부 - 유효성 검사
    // TODO : URL 생김새에 대해 논의필요
    @Operation(summary = "대기 방 존재 여부 검사", description = "url을 통해 대기 방 존재 여부를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대기 방 존재 x (생성 가능)", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindWaitingRoomRes.class))}),
            @ApiResponse(responseCode = "400", description = "대기 방 존재 o (생성 불가)", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindWaitingRoomRes.class))}),
    })
    @GetMapping("/exist/{url}")
    public ResponseEntity<?> findWaitingRoom(@PathVariable(value = "url") String url) {
        return waitingRoomService.findWaitingRoom(url);
    }

    // Description : 현재 대기 인원 조회 - // 참여할 수 있는지 확인해봐야 하므로 ??
    // TODO : OK
    @Operation(summary = "현재 대기 인원 조회", description = "waitingRoom id를 통해 현재 대기 인원을 조회하여 참여 가능한 지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대기 방 참여 가능", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindWaitingRoomWithCountRes.class))}),
            @ApiResponse(responseCode = "400", description = "대기 방 참여 불가", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = JoinWaitingRoomRes.class))}),
    })
    @GetMapping("/{waiting-room-url}")
    public ResponseEntity<?> checkCanJoinWaitingRoom(@PathVariable(value = "waiting-room-url") String url) {
        return waitingRoomService.checkCanJoinWaitingRoom(url);
    }
}

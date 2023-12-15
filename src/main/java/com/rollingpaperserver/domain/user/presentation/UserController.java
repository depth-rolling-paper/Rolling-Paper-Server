package com.rollingpaperserver.domain.user.presentation;

import com.rollingpaperserver.domain.user.application.UserService;
import com.rollingpaperserver.domain.user.dto.request.CreateUserReq;
import com.rollingpaperserver.domain.user.dto.response.CreateUserRes;
import com.rollingpaperserver.domain.user.dto.response.ExclusionMeRes;
import com.rollingpaperserver.domain.user.dto.response.FindUserRes;
import com.rollingpaperserver.domain.waitingRoom.dto.response.CreateWaitingRoomRes;
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

@Tag(name = "User API", description = "User 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Description : 롤링페이퍼 쓰러 가기 버튼 - 아래 두 개 호출
     */
    
    // Description : 유저 닉네임 유효성 검사
    // TODO : OK
    @Operation(summary = "유저 닉네임 유효성 검사", description = "유저 닉네임 유효성을 검사합니다. (중복 확인)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 닉네임", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindUserRes.class))}),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 닉네임 (사용 불가)", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindUserRes.class))}),
    })
    @GetMapping("/{userName}/{url}")
    public ResponseEntity<?> checkUser(@PathVariable(value = "userName") String userName, @PathVariable(value = "url") String url) {
        return userService.checkUser(userName, url);
    }

    // Description : 유저 생성 :: 롤링페이퍼 작성하기 버튼 (waiting room 포함) --> 바로 waiting room 입장하는 것 !
    // TODO : OK
    @Operation(summary = "유저 생성과 동시에 대기 방 입장", description = "유저 생성과 동시에 대기 방에 입장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 생성 및 대기 방 입장 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserRes.class))}),
            @ApiResponse(responseCode = "400", description = "유저 생성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserRes.class))}),
    })
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserReq createUserReq) {
        return userService.createUser(createUserReq);
    }

    // Description : 본인 제외 유저 목록 조회 (방 내에서)
    // TODO : OK
    @Operation(summary = "본인 제외 유저 목록 조회", description = "user-id와 room-id를 통해 같은 방에 있는 본인을 제외한 유저 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "본인 제외 현재 방 내 유저 목록 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExclusionMeRes.class))}),
            @ApiResponse(responseCode = "400", description = "본인 제외 현재 방 내 유저 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = FindUserRes.class))}),
    })
    @GetMapping("/exclusion/{user-id}/{room-id}")
    public ResponseEntity<?> findUsersExclusionMe(@PathVariable(value = "user-id") Long userId, @PathVariable(value = "room-id") Long roomId ) {
        return userService.findUsersExclusionMe(userId, roomId);
    }

    // Description : 방 나가기 / 마지막 유저가 방 나갈 시 방도 함께 삭제
    @DeleteMapping("/{user-id}/{room-id}")
    public ResponseEntity<?> outRoom(@PathVariable(value = "user-id") Long userId, @PathVariable(value = "room-id") Long roomId) {
        return userService.outRoom(userId, roomId);
    }

}

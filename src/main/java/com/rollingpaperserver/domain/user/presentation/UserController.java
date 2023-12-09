package com.rollingpaperserver.domain.user.presentation;

import com.rollingpaperserver.domain.user.application.UserService;
import com.rollingpaperserver.domain.user.dto.request.CreateUserReq;
import com.rollingpaperserver.domain.user.dto.request.JoinWaitingRoomReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Description : 롤링페이퍼 쓰러 가기 버튼 - 아래 두 개 호출
     */

    // Description : 유저 생성
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserReq createUserReq) {
        return userService.createUser(createUserReq);
    }

    // Description : 유저 - 대기 방 입장
    @PostMapping("/{url}/{user-id}")
    public ResponseEntity<?> joinWaitingRoom(@PathVariable(value = "url") String waitingRoomUrl, @PathVariable(value = "user-id") Long userId) {
        return userService.joinWaitingRoom(waitingRoomUrl, userId);

    }

    // Description : 유저명 조회
    @GetMapping("/{userName}")
    public ResponseEntity<?> findUserByName(@PathVariable(value = "userName") String userName) {
        return userService.findUserByName(userName);
    }

    // Description : 본인 제외 유저 목록 조회
    @GetMapping("/exclusion/{user-id}")
    public ResponseEntity<?> findUsersExclusionMe(@PathVariable(value = "user-id") Long userId) {
        return userService.findUsersExclusionMe(userId);
    }

    // Description : 유저 :: 대기방 -> 방 입장 (선행 : 대기방에 매핑될 방이 생성되어 있어야 함)


}

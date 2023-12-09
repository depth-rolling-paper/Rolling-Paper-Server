package com.rollingpaperserver.domain.user.presentation;

import com.rollingpaperserver.domain.user.application.UserService;
import com.rollingpaperserver.domain.user.dto.request.CreateUserReq;
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
    
    // Description : 유저 닉네임 유효성 검사
    // TODO : OK
    @GetMapping("/{userName}")
    public ResponseEntity<?> checkUser(@PathVariable(value = "userName") String userName) {
        return userService.checkUser(userName);
    }

    // Description : 유저 생성 :: 롤링페이퍼 작성하기 버튼 (waiting room 포함) --> 바로 waiting room 입장하는 것 !
    // TODO : OK / 입장 순서 생각하기 (Integer seq)
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserReq createUserReq) {
        return userService.createUser(createUserReq);
    }

    // Description : 본인 제외 유저 목록 조회 (방 내에서)
    // TODO : OK
    @GetMapping("/exclusion/{user-id}")
    public ResponseEntity<?> findUsersExclusionMe(@PathVariable(value = "user-id") Long userId) {
        return userService.findUsersExclusionMe(userId);
    }

    // Description : 유저 :: 대기방 -> 방 입장 (선행 : 대기방에 매핑될 방이 생성되어 있어야 함)


}

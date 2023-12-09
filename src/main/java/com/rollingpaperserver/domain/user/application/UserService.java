package com.rollingpaperserver.domain.user.application;

import com.rollingpaperserver.domain.user.domain.User;
import com.rollingpaperserver.domain.user.domain.repository.UserRepository;
import com.rollingpaperserver.domain.user.dto.request.CreateUserReq;
import com.rollingpaperserver.domain.user.dto.response.CreateUserRes;
import com.rollingpaperserver.domain.user.dto.response.ExclusionMeRes;
import com.rollingpaperserver.domain.user.dto.response.FindUserRes;
import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import com.rollingpaperserver.domain.waitingRoom.domain.repository.WaitingRoomRepository;
import com.rollingpaperserver.domain.waitingRoom.dto.response.FindWaitingRoomRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final WaitingRoomRepository waitingRoomRepository;

    // Description : 유저 닉네임 유효성 검사
    // TODO : OK
    public ResponseEntity<?> checkUser(String userName) {

        Optional<User> userByUserName = userRepository.findByUserName(userName);
        if (userByUserName.isPresent()) {
            FindUserRes findUserRes = FindUserRes.builder()
                    .message("이미 존재하는 닉네임입니다.")
                    .build();

            return ResponseEntity.badRequest().body(findUserRes);
        }

        FindUserRes findUserRes = FindUserRes.builder()
                .message("사용 가능한 닉네임입니다.")
                .build();

        return ResponseEntity.ok(findUserRes);

    }

    // Description : User 생성 / 대기 방 생성 후
    // TODO : OK
    @Transactional
    public ResponseEntity<?> createUser(CreateUserReq createUserReq) {

        // 대기 방 존재 여부 확인
        Optional<WaitingRoom> waitingRoomByUrl = waitingRoomRepository.findByUrl(createUserReq.getWaitingRoomUrl());
        if (!waitingRoomByUrl.isPresent()) {
            CreateUserRes createUserRes = CreateUserRes.builder()
                    .id(null)
                    .message("존재하지 않는 대기 방입니다.")
                    .build();

            return ResponseEntity.badRequest().body(createUserRes);
        }

        WaitingRoom waitingRoom = waitingRoomByUrl.get();

        // 대기 방 내에서 유저 이름 중복 체크
        for (int i = 0; i < waitingRoom.getUsers().size(); i++) {
            if (createUserReq.getUserName().equals(waitingRoom.getUsers().get(i).getUserName())) {
                CreateUserRes createUserRes = CreateUserRes.builder()
                        .id(waitingRoom.getUsers().get(i).getId())
                        .message("대기 방에 같은 이름의 유저가 존재합니다. (ID는 이미 존재하는 유저 ID)")
                        .build();

                return ResponseEntity.badRequest().body(createUserRes);
            }
        }

        User user = User.builder()
                .userName(createUserReq.getUserName())
                .userType(createUserReq.getUserType())
                .room(null)
                .waitingRoom(waitingRoom)
                .build();

        userRepository.save(user);
        waitingRoom.updateCurrentUserNum(waitingRoom.getCurrent_user_num() + 1);

        CreateUserRes createUserRes = CreateUserRes.builder()
                .id(user.getId())
                .message("유저가 생성되었습니다. (ID는 생성된 유저 ID)")
                .build();

        return ResponseEntity.ok(createUserRes);

    }

    // Description : 본인 제외 유저 목록 조회 / 일단 대기 방으로 했음 --> 추후 방으로 해야할 듯
    // TODO : 지연로딩 관련 문제 (findAllUsers에서 waitingRoom을 함께 가져와야 하는 문제가 발생함)
    // TODO : OK
    public ResponseEntity<?> findUsersExclusionMe(Long userId) {

        Optional<User> findUserWithRoomAndWaitingRoom = userRepository.findUserWithRoomAndWaitingRoom(userId);
//        Optional<User> findUserById = userRepository.findById(userId);
        if (!findUserWithRoomAndWaitingRoom.isPresent()) {
            FindUserRes findUserRes = FindUserRes.builder()
                    .message("유저가 존재하지 않습니다.")
                    .build();

            return ResponseEntity.badRequest().body(findUserRes);
        }

        User user = findUserWithRoomAndWaitingRoom.get();

        WaitingRoom waitingRoom = user.getWaitingRoom();

        List<User> users = waitingRoom.getUsers();
        List<User> exclusiveMe = new ArrayList<>();

        for (User userInWaitingRoom : users) {
            if (userInWaitingRoom.getId() == userId)
                continue;

            exclusiveMe.add(userInWaitingRoom);
        }

        ExclusionMeRes exclusionMeRes = ExclusionMeRes.builder()
                .users(exclusiveMe)
                .message("본인 제외 현재 대기방 내 유저 목록")
                .build();

        return ResponseEntity.ok(exclusionMeRes);

    }


}

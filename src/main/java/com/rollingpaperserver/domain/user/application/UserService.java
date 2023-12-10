package com.rollingpaperserver.domain.user.application;

import com.rollingpaperserver.domain.room.domain.Room;
import com.rollingpaperserver.domain.room.domain.repository.RoomRepository;
import com.rollingpaperserver.domain.user.domain.User;
import com.rollingpaperserver.domain.user.domain.repository.UserRepository;
import com.rollingpaperserver.domain.user.dto.UserDTO;
import com.rollingpaperserver.domain.user.dto.request.CreateUserReq;
import com.rollingpaperserver.domain.user.dto.response.CreateUserRes;
import com.rollingpaperserver.domain.user.dto.response.ExclusionMeRes;
import com.rollingpaperserver.domain.user.dto.response.FindUserRes;
import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import com.rollingpaperserver.domain.waitingRoom.domain.repository.WaitingRoomRepository;
import com.rollingpaperserver.domain.waitingRoom.dto.response.FindWaitingRoomRes;
import com.rollingpaperserver.domain.waitingRoom.dto.response.JoinWaitingRoomRes;
import com.rollingpaperserver.global.config.WebSocketEventListener;
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
    private final RoomRepository roomRepository;

    private final WebSocketEventListener webSocketEventListener;

    // Description : 유저 닉네임 유효성 검사
    // TODO : OK - 대기 방 내에서 유효성 검사할 필요가 있음 : 해결
    public ResponseEntity<?> checkUser(String userName, String url) {

        Optional<WaitingRoom> waitingRoomByUrl = waitingRoomRepository.findByUrl(url);
        if (waitingRoomByUrl.isEmpty()) {
            FindUserRes findUserRes = FindUserRes.builder()
                    .canUse(false)
                    .message("해당 url의 대기 방이 존재하지 않습니다.")
                    .build();

            return ResponseEntity.badRequest().body(findUserRes);
        }

        WaitingRoom waitingRoom = waitingRoomByUrl.get();
        List<User> allByWaitingRoom = userRepository.findAllByWaitingRoom(waitingRoom);
        List<UserDTO> userDTOS = new ArrayList<>();

        for (User user : allByWaitingRoom) {
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .userName(user.getUserName())
                    .build();
            userDTOS.add(userDTO);
        }

        for (UserDTO userDTO : userDTOS) {
            if (userDTO.getUserName().equals(userName)) {
                FindUserRes findUserRes = FindUserRes.builder()
                        .canUse(false)
                        .message("이미 존재하는 닉네임입니다.")
                        .build();

                return ResponseEntity.badRequest().body(findUserRes);
            }
        }

        FindUserRes findUserRes = FindUserRes.builder()
                .canUse(true)
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
                        .message("대기 방에 같은 이름의 유저가 존재합니다. (이미 존재하는 유저 ID 반환)")
                        .build();

                return ResponseEntity.badRequest().body(createUserRes);
            }
        }

        if (waitingRoom.getCurrent_user_num() >= waitingRoom.getLimit_user_num()) {
            JoinWaitingRoomRes joinWaitingRoomRes = JoinWaitingRoomRes.builder()
                    .canJoin(false)
                    .message("대기 방이 가득 찼습니다.")
                    .build();

            return ResponseEntity.badRequest().body(joinWaitingRoomRes);
        }

        User user = User.builder()
                .userName(createUserReq.getUserName())
                .userType(createUserReq.getUserType())
                .room(null)
                .waitingRoom(waitingRoom)
                .sequence(waitingRoom.getCurrent_user_num() + 1)
                .build();

        userRepository.save(user);
        waitingRoom.updateCurrentUserNum(waitingRoom.getCurrent_user_num() + 1);

        // 소켓 연결된 녀석들에게 실시간 유저 수 전송
        webSocketEventListener.sendUserCountUpdate(waitingRoom.getUrl(), waitingRoom.getCurrent_user_num());

        CreateUserRes createUserRes = CreateUserRes.builder()
                .id(user.getId())
                .message("유저가 생성되었습니다. (생성된 유저 ID)")
                .build();

        return ResponseEntity.ok(createUserRes);

    }

    // Description : 본인 제외 유저 목록 조회 / 일단 대기 방으로 했음 --> 방으로 바꾸어야 함 --> 바꿈
    // TODO : OK
    public ResponseEntity<?> findUsersExclusionMe(Long userId, Long roomId) {

        Optional<Room> roomById = roomRepository.findById(roomId);
        if (roomById.isEmpty()) {
            FindUserRes findUserRes = FindUserRes.builder()
                    .message("방이 존재하지 않습니다.")
                    .build();

            return ResponseEntity.badRequest().body(findUserRes);
        }

        Optional<User> findUserWithRoomAndWaitingRoom = userRepository.findUserWithRoomAndWaitingRoom(userId);
//        Optional<User> findUserById = userRepository.findById(userId);
        if (!findUserWithRoomAndWaitingRoom.isPresent()) {
            FindUserRes findUserRes = FindUserRes.builder()
                    .message("유저가 존재하지 않습니다.")
                    .build();

            return ResponseEntity.badRequest().body(findUserRes);
        }

        User user = findUserWithRoomAndWaitingRoom.get();

//        WaitingRoom waitingRoom = user.getWaitingRoom();
        Room room = user.getRoom();

        List<User> users = room.getUsers();
        List<User> exclusiveMe = new ArrayList<>();

        for (User userInRoom : users) {
            if (userInRoom.getId() == userId)
                continue;

            exclusiveMe.add(userInRoom);
        }

        ExclusionMeRes exclusionMeRes = ExclusionMeRes.builder()
                .users(exclusiveMe)
                .message("본인 제외 현재 방 내 유저 목록")
                .build();

        return ResponseEntity.ok(exclusionMeRes);

    }


}

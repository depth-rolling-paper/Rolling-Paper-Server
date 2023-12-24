package com.rollingpaperserver.domain.user.application;

import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaper;
import com.rollingpaperserver.domain.rollingPaper.domain.repository.RollingPaperRepository;
import com.rollingpaperserver.domain.room.domain.Room;
import com.rollingpaperserver.domain.room.domain.repository.RoomRepository;
import com.rollingpaperserver.domain.room.dto.response.FindRoomRes;
import com.rollingpaperserver.domain.user.domain.User;
import com.rollingpaperserver.domain.user.domain.repository.UserRepository;
import com.rollingpaperserver.domain.user.dto.UserDTO;
import com.rollingpaperserver.domain.user.dto.request.CreateUserReq;
import com.rollingpaperserver.domain.user.dto.response.*;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final WaitingRoomRepository waitingRoomRepository;
    private final RoomRepository roomRepository;
    private final RollingPaperRepository rollingPaperRepository;

    private final WebSocketEventListener webSocketEventListener;

    // Description : 유저 닉네임 유효성 검사
    // TODO : OK
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

        // 현재 인원 == 최대 인원인 경우
        if (waitingRoom.getCurrent_user_num() == waitingRoom.getLimit_user_num()) {
            JoinWaitingRoomRes joinWaitingRoomRes = JoinWaitingRoomRes.builder()
                    .canJoin(false)
                    .message("이 방은 더 이상 입장이 불가능해요, 인원 수를 확인해 주세요.")
                    .build();

            return ResponseEntity.badRequest().body(joinWaitingRoomRes);
        }

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
                        .message("이미 사용 중인 이름이에요.")
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

        // Description : Socket Message Send 소켓 연결된 녀석들에게 실시간 유저 수 전송
        webSocketEventListener.sendUserCountUpdate(waitingRoom.getUrl(), waitingRoom.getCurrent_user_num());

        CreateUserRes createUserRes = CreateUserRes.builder()
                .id(user.getId())
                .limitUserCount(waitingRoom.getLimit_user_num())
                .currentUserCount(waitingRoom.getCurrent_user_num())
                .message("유저가 생성되었습니다. (생성된 유저 ID)")
                .build();

        return ResponseEntity.ok(createUserRes);

    }

    // Description : 해당 '방' 내의 본인 제외 유저 목록 조회
    // TODO : OK
    public ResponseEntity<?> findUsersExclusionMe(Long userId,  String roomUrl) {

//        Optional<Room> roomById = roomRepository.findById(roomId);
        Optional<Room> roomById = roomRepository.findByUrl(roomUrl);
        if (roomById.isEmpty()) {
            FindUserRes findUserRes = FindUserRes.builder()
                    .message("방이 존재하지 않습니다.")
                    .build();

            return ResponseEntity.badRequest().body(findUserRes);
        }

        Room room = roomById.get();

        Optional<User> userById = userRepository.findById(userId);
        if (userById.isEmpty()) {
            FindUserRes findUserRes = FindUserRes.builder()
                    .message("유저가 존재하지 않습니다.")
                    .build();

            return ResponseEntity.badRequest().body(findUserRes);
        }

        User user = userById.get();

        List<User> allUsersInRoom = userRepository.findAllByRoom(room);
        List<UserRes> exclusiveMe = new ArrayList<>();

        for (User userInRoom : allUsersInRoom) {
            if (userInRoom.getId() == userId)
                continue;

            // userId를 기준으로 뒤쪽으로 회전시키기 위한 offset 계산
            int offset = (int) (userInRoom.getId() - userId);

            // 겹치지 않게 sequence를 조합
            int newSequence = (offset > 0) ? offset : allUsersInRoom.size() + offset;

            UserRes userRes = UserRes.builder()
                    .id(userInRoom.getId())
                    .userName(userInRoom.getUserName())
                    .userType(userInRoom.getUserType())
                    .sequence(newSequence)
                    .build();

            exclusiveMe.add(userRes);
        }

        exclusiveMe.sort(Comparator.comparing(UserRes::getSequence));

        ExclusionMeRes exclusionMeRes = ExclusionMeRes.builder()
                .roomName(room.getRoomName())
                .users(exclusiveMe)
                .message("본인 제외 현재 방 내 유저 목록")
                .build();

        return ResponseEntity.ok(exclusionMeRes);
    }

    // Description : 방 나가기 / 마지막 유저가 방 나갈 시 방도 함께 삭제
    @Transactional
    public ResponseEntity<?> outRoom(Long userId, String roomUrl) {

        // 방 존재 확인
//        Optional<Room> byRoomId = roomRepository.findById(roomId);
        Optional<Room> byRoomId = roomRepository.findByUrl(roomUrl);

        if (byRoomId.isEmpty()) {
            FindRoomRes roomRes = FindRoomRes.builder()
                    .message("해당 ID의 방이 존재하지 않습니다.")
                    .build();

            return ResponseEntity.badRequest().body(roomRes);
        }

        Room room = byRoomId.get();

        // 유저 존재 확인
        Optional<User> byUserId = userRepository.findById(userId);

        if (byUserId.isEmpty()) {
            FindRoomRes roomRes = FindRoomRes.builder()
                    .message("해당 ID의 유저가 존재하지 않습니다.")
                    .build();

            return ResponseEntity.badRequest().body(roomRes);
        }

        // 방에서 해당 유저 이탈
        User user = byUserId.get();

        user.updateRoom(null);
        room.getUsers().remove(user);

        // Description : Socket Message Send 소켓 연결된 녀석들에게 실시간 유저 수 전송
        webSocketEventListener.sendUserCountUpdate(room.getUrl(), room.getUsers().size());

        if (room.getUsers().isEmpty()) {
            // If : 유저가 모두 이탈했으면
            OutRoomRes outRoomRes = OutRoomRes.builder()
                    .emptyRoom(true)
                    .userCountInRoom(0)
                    .message("해당 방의 유저가 모두 이탈하였습니다. 방을 삭제합니다.")
                    .build();

            roomRepository.delete(room);

            return ResponseEntity.ok(outRoomRes);
        } else {
            OutRoomRes outRoomRes = OutRoomRes.builder()
                    .emptyRoom(false)
                    .userCountInRoom(room.getUsers().size())
                    .message("아직 롤링페이퍼를 작성 중인 유저가 존재합니다.")
                    .build();

            return ResponseEntity.ok(outRoomRes);
        }

    }

    // Description : 유저 삭제 및 롤링페이퍼 삭제 - 이미지 저장까지 한 진짜 마지막
    @Transactional
    public ResponseEntity<?> deleteUser(Long userId) {

        // 유저 존재 확인
        Optional<User> byUserId = userRepository.findById(userId);

        if (byUserId.isEmpty()) {
            FindRoomRes roomRes = FindRoomRes.builder()
                    .message("해당 ID의 유저가 존재하지 않습니다.")
                    .build();

            return ResponseEntity.badRequest().body(roomRes);
        }

        User user = byUserId.get();

        List<RollingPaper> rollingPaperList = rollingPaperRepository.findByUser(user);
        rollingPaperRepository.deleteAll(rollingPaperList);

        userRepository.delete(user);

        CreateUserRes createUserRes = CreateUserRes.builder()
                .id(userId)
                .message("유저 및 해당 유저의 롤링페이퍼가 삭제되었습니다.")
                .build();

        return ResponseEntity.ok(createUserRes);
    }
}

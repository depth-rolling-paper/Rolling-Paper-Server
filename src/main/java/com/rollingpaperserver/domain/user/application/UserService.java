package com.rollingpaperserver.domain.user.application;

import com.rollingpaperserver.domain.user.domain.User;
import com.rollingpaperserver.domain.user.domain.repository.UserRepository;
import com.rollingpaperserver.domain.user.dto.request.CreateUserReq;
import com.rollingpaperserver.domain.user.dto.response.CreateUserRes;
import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import com.rollingpaperserver.domain.waitingRoom.domain.repository.WaitingRoomRepository;
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

    // TODO : 유저 조회 확인  --> DB로 확인
    // Description : 응답 시 반환은 무조건 ResDto
    
    // Description : User 생성 / 대기 방 생성 후
    // TODO : 대기 방 입장이랑 합치는 것 고민해 볼 필요 있을 듯
    @Transactional
    public ResponseEntity<?> createUser(CreateUserReq createUserReq) {

        // 이름 중복 체크 필요
        Optional<User> userByUserName = userRepository.findByUserName(createUserReq.getUserName());
        if (userByUserName.isPresent())
            return ResponseEntity.badRequest().body("이미 존재하는 닉네임입니다.");

        User user = User.builder()
                .userName(createUserReq.getUserName())
                .userType(createUserReq.getUserType())
                .room(null)
                .waitingRoom(null)
                .build();

        userRepository.save(user);

        CreateUserRes createUserRes = CreateUserRes.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .userType(user.getUserType())
                .room(user.getRoom())
                .waitingRoom(user.getWaitingRoom())
                .build();

        return ResponseEntity.ok(createUserRes);

    }

    // Description : 유저 -> 대기 방 입장
    @Transactional
    public ResponseEntity<?> joinWaitingRoom(String waitingRoomUrl, Long userId) {

        Optional<WaitingRoom> waitingRoomByUrl = waitingRoomRepository.findByUrl(waitingRoomUrl);

        // 해당 id의 대기 방이 없는 경우
        if (!waitingRoomByUrl.isPresent()) {
//            throw new IllegalStateException("없는 대기 방입니다.");
            return ResponseEntity.badRequest().body("없는 대기 방입니다.");
        }

        WaitingRoom waitingRoom = waitingRoomByUrl.get();

        Optional<User> userById = userRepository.findById(userId);
        if (!userById.isPresent())
            return ResponseEntity.badRequest().body("없는 유저입니다.");

        User user = userById.get();

        if (user.getWaitingRoom() != null)
            return ResponseEntity.badRequest().body("이미 대기방에 입장하였습니다.");

        // 방 배정 -> 방 입장
        user.updateWaitingRoom(waitingRoom);
        waitingRoom.updateCurrentUserNum(waitingRoom.getCurrent_user_num() + 1);

        return ResponseEntity.ok("대기 방 입장 성공");
    }

    // Description : 닉네임으로 유저 조회 ( 유효성 검사 시 사용 )
    public ResponseEntity<?> findUserByName(String userName) {

        Optional<User> byUserName = userRepository.findByUserName(userName);
        if (byUserName.isPresent())
            return ResponseEntity.badRequest().body("이미 존재하는 닉네임입니다.");

        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    // Description : 본인 제외 유저 목록 조회 / 일단 대기 방으로 했음 --> 추후 방으로 해야할 지 고민 필요
    // TODO : 지연로딩 관련 문제 (findAllUsers에서 waitingRoom을 함께 가져와야 하는 문제가 발생함)
    public ResponseEntity<?> findUsersExclusionMe(Long userId) {

        List<User> findAllUsers = userRepository.findAllWithWaitingRoom();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < findAllUsers.size(); i++) {
            if (findAllUsers.get(i).getId() == userId)
                continue;

            users.add(findAllUsers.get(i));
        }
        return ResponseEntity.ok(users);

    }
}

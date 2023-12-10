package com.rollingpaperserver.domain.room.application;

import com.rollingpaperserver.domain.room.domain.Room;
import com.rollingpaperserver.domain.room.domain.repository.RoomRepository;
import com.rollingpaperserver.domain.room.dto.request.CreateRoomReq;
import com.rollingpaperserver.domain.room.dto.response.CreateRoomRes;
import com.rollingpaperserver.domain.user.domain.User;
import com.rollingpaperserver.domain.user.domain.repository.UserRepository;
import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import com.rollingpaperserver.domain.waitingRoom.domain.repository.WaitingRoomRepository;
import com.rollingpaperserver.domain.waitingRoom.dto.response.FindWaitingRoomRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final WaitingRoomRepository waitingRoomRepository;
    private final UserRepository userRepository;

    // Description : 방 생성 + 유저 이동 포함
    @Transactional
    public ResponseEntity<?> createRoom(CreateRoomReq createRoomReq) {

        Optional<WaitingRoom> waitingRoomByWaitingRoomName = waitingRoomRepository.findByWaitingRoomName(createRoomReq.getRoomName());
        if (!waitingRoomByWaitingRoomName.isPresent()) {
            FindWaitingRoomRes findWaitingRoomRes = FindWaitingRoomRes.builder()
                    .message("해당 이름의 대기 방이 존재하지 않습니다.")
                    .build();

            return ResponseEntity.badRequest().body(findWaitingRoomRes);
        }

        WaitingRoom waitingRoom = waitingRoomByWaitingRoomName.get();

        Room room = Room.builder()
                .roomName(createRoomReq.getRoomName())
                .build();

        roomRepository.save(room);

        // Description : 해당 대기 방 유저들 --> 방으로 입장
        List<User> users = waitingRoom.getUsers();
        for (User user : users) {
            user.updateRoom(room);
        }

        CreateRoomRes createRoomRes = CreateRoomRes.builder()
                .id(room.getId())
                .roomName(room.getRoomName())
                .message("방이 생성되었습니다. 동시에 대기 방의 유저들이 방으로 입장하였습니다.")
                .build();

        return ResponseEntity.ok(createRoomRes);
    }
}

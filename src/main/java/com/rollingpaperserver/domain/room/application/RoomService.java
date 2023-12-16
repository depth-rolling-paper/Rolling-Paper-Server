package com.rollingpaperserver.domain.room.application;

import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaper;
import com.rollingpaperserver.domain.rollingPaper.domain.repository.RollingPaperRepository;
import com.rollingpaperserver.domain.room.domain.Room;
import com.rollingpaperserver.domain.room.domain.repository.RoomRepository;
import com.rollingpaperserver.domain.room.dto.request.CreateRoomReq;
import com.rollingpaperserver.domain.room.dto.request.DeleteRoomReq;
import com.rollingpaperserver.domain.room.dto.response.CreateRoomRes;
import com.rollingpaperserver.domain.room.dto.response.DeleteRoomRes;
import com.rollingpaperserver.domain.room.dto.response.FindRoomRes;
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
    private final RollingPaperRepository rollingPaperRepository;

    // Description : 방 생성 + 유저 이동 포함
    @Transactional
    public ResponseEntity<?> createRoom(String url) {

//        Optional<WaitingRoom> waitingRoomByWaitingRoomName = waitingRoomRepository.findByWaitingRoomName(createRoomReq.getRoomName());
        Optional<WaitingRoom> waitingRoomByWaitingRoomUrl = waitingRoomRepository.findByUrl(url);
        if (!waitingRoomByWaitingRoomUrl.isPresent()) {
            FindWaitingRoomRes findWaitingRoomRes = FindWaitingRoomRes.builder()
                    .canMake(true)
                    .message("해당 url의 대기 방이 존재하지 않습니다.")
                    .build();

            return ResponseEntity.badRequest().body(findWaitingRoomRes);
        }

        WaitingRoom waitingRoom = waitingRoomByWaitingRoomUrl.get();

        Room room = Room.builder()
//                .roomName(createRoomReq.getRoomName())
                .roomName(waitingRoom.getWaitingRoomName())
                .url(waitingRoom.getUrl())
                .build();

        roomRepository.save(room);

        // Description : 해당 대기 방 유저들 --> 방으로 입장
        List<User> users = waitingRoom.getUsers();
        for (User user : users) {
            user.updateRoom(room);

            // Description : 유저에 할당된 대기 방 삭제
            user.updateWaitingRoom(null);

        }

        // Description : 대기 방 삭제
        waitingRoomRepository.delete(waitingRoom);

        CreateRoomRes createRoomRes = CreateRoomRes.builder()
                .id(room.getId())
                .roomName(room.getRoomName())
                .message("방이 생성되었습니다. 동시에 대기 방의 유저들이 방으로 입장하였습니다.")
                .build();

        return ResponseEntity.ok(createRoomRes);
    }

    // Description : 방 삭제
    @Transactional
    public ResponseEntity<?> deleteRoom(DeleteRoomReq deleteRoomReq) {
        Optional<Room> byRoomId = roomRepository.findById(deleteRoomReq.getRoomId());

        if (byRoomId.isEmpty()) {
            FindRoomRes roomRes = FindRoomRes.builder()
                    .message("해당 ID의 방이 존재하지 않습니다.")
                    .build();

            return ResponseEntity.badRequest().body(roomRes);
        }

        Room room = byRoomId.get();

        List<User> users = room.getUsers();
        for (User user : users) {
            user.updateRoom(null);
        }

        roomRepository.delete(room);

        for (User user : users) {
            List<RollingPaper> rollingPapers = user.getRollingPapers();
            for (RollingPaper rollingPaper : rollingPapers) {
                // Description : rolling-paper 삭제
                rollingPaperRepository.delete(rollingPaper);
            }
            // Description: user 삭제
            userRepository.delete(user);
        }

        DeleteRoomRes deleteRoomRes = DeleteRoomRes.builder()
                .id(room.getId())
                .roomName(room.getRoomName())
                .message("방이 삭제되었습니다.")
                .build();

        return ResponseEntity.ok(deleteRoomRes);
    }
}

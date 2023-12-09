package com.rollingpaperserver.domain.room.application;

import com.rollingpaperserver.domain.room.domain.Room;
import com.rollingpaperserver.domain.room.domain.repository.RoomRepository;
import com.rollingpaperserver.domain.room.dto.request.CreateRoomReq;
import com.rollingpaperserver.domain.room.dto.response.CreateRoomRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;

    // Description : 방 생성
    @Transactional
    public ResponseEntity<?> createRoom(CreateRoomReq createRoomReq) {

        Room room = Room.builder()
                .roomName(createRoomReq.getRoomName())
                .build();

        roomRepository.save(room);

        CreateRoomRes createRoomRes = CreateRoomRes.builder()
                .id(room.getId())
                .roomName(room.getRoomName())
                .build();

        return ResponseEntity.ok(createRoomRes);
    }
}

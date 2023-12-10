package com.rollingpaperserver.domain.waitingRoom.application;

import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import com.rollingpaperserver.domain.waitingRoom.domain.repository.WaitingRoomRepository;
import com.rollingpaperserver.domain.waitingRoom.dto.response.CreateWaitingRoomRes;
import com.rollingpaperserver.domain.waitingRoom.dto.response.FindWaitingRoomRes;
import com.rollingpaperserver.domain.waitingRoom.dto.response.FindWaitingRoomWithCountRes;
import com.rollingpaperserver.domain.waitingRoom.dto.response.JoinWaitingRoomRes;
import com.rollingpaperserver.domain.waitingRoom.dto.resquest.CreateWaitingRoomReq;
import com.rollingpaperserver.global.config.WebSocketEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaitingRoomService {

    private final WaitingRoomRepository waitingRoomRepository;

    // Description : 대기 방 생성
    // TODO : OK
    @Transactional
    public ResponseEntity<?> createWaitingRoom(CreateWaitingRoomReq createWaitingRoomReq) {
        // URL 생성
        String url = UUID.randomUUID().toString();

        // 대기 방 생성
        WaitingRoom newWaitingRoom = WaitingRoom.builder()
                .waiting_room_name(createWaitingRoomReq.getWaiting_room_name())
                .limit_user_num(createWaitingRoomReq.getLimit_user_num())
                .start_time(LocalDateTime.now().plusMinutes(createWaitingRoomReq.getStart_time_minute()))
                .current_user_num(0) // 대기 방 생성 시점 -> 아직 방장조차 입장 x -> 현재 인원 0명으로 시작
                .url(url)
                .build();

        // 대기 방 생성 후 저장
        waitingRoomRepository.save(newWaitingRoom);

        CreateWaitingRoomRes createWaitingRoomRes = CreateWaitingRoomRes.builder()
                .url(newWaitingRoom.getUrl())
                .message("대기 방 URL을 반환합니다.")
                .build();

        return ResponseEntity.ok(createWaitingRoomRes);

    }

    // Description : url과 일치하는 대기 방 존재 여부
    public ResponseEntity<?> findWaitingRoom(String url) {
        Optional<WaitingRoom> findByUrlWaitingRoom = waitingRoomRepository.findByUrl(url);

        // 해당 url의 대기 방이 없는 경우
        if (!findByUrlWaitingRoom.isPresent()) {
            FindWaitingRoomRes findWaitingRoomRes = FindWaitingRoomRes.builder()
                    .canMake(true)
                    .message("없는 대기 방입니다.")
                    .build();
            return ResponseEntity.ok(findWaitingRoomRes);
        }

        FindWaitingRoomRes findWaitingRoomRes = FindWaitingRoomRes.builder()
                .canMake(false)
                .message("대기 방이 존재합니다.")
                .build();

        return ResponseEntity.badRequest().body(findWaitingRoomRes);

    }

    // Description : 현재 대기 인원 조회 - 참여할 수 있는지 확인해봐야 하므로
    public ResponseEntity<?> checkCanJoinWaitingRoom(Long waitingRoomId) {
        Optional<WaitingRoom> findByIdWaitingRoom = waitingRoomRepository.findById(waitingRoomId);

        // 해당 대기 방이 없는 경우
        if (!findByIdWaitingRoom.isPresent()) {
            JoinWaitingRoomRes joinWaitingRoomRes = JoinWaitingRoomRes.builder()
                    .canJoin(false)
                    .message("존재하지 않는 대기 방입니다.")
                    .build();

            return ResponseEntity.badRequest().body(joinWaitingRoomRes);
        }

        WaitingRoom waitingRoom = findByIdWaitingRoom.get();

        // 현재 인원 == 최대 인원인 경우
        if (waitingRoom.getCurrent_user_num() == waitingRoom.getLimit_user_num()) {
            JoinWaitingRoomRes joinWaitingRoomRes = JoinWaitingRoomRes.builder()
                    .canJoin(false)
                    .message("대기 방이 가득 찼습니다.")
                    .build();

            return ResponseEntity.badRequest().body(joinWaitingRoomRes);
        }

        FindWaitingRoomWithCountRes findWaitingRoomWithCountRes = FindWaitingRoomWithCountRes.builder()
                .canJoin(true)
                .currentUserCount(waitingRoom.getCurrent_user_num())
                .message("대기 방에 입장 가능합니다.")
                .build();

        return ResponseEntity.ok(findWaitingRoomWithCountRes);

    }
}

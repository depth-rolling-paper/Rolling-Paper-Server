package com.rollingpaperserver.domain.waitingRoom.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CreateWaitingRoomRes {

    private Long id;

    private String waiting_room_name;

    private int limit_user_num;

    private int current_user_num;

    private LocalDateTime start_time;

    private String url;

    @Builder
    public CreateWaitingRoomRes(Long id, String waiting_room_name, int limit_user_num, int current_user_num, LocalDateTime start_time, String url) {
        this.id = id;
        this.waiting_room_name = waiting_room_name;
        this.limit_user_num = limit_user_num;
        this.current_user_num = current_user_num;
        this.start_time = start_time;
        this.url = url;
    }
}

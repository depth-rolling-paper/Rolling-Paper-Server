package com.rollingpaperserver.domain.waitingRoom.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CreateWaitingRoomRes {

    private Long id;

    private String url;

    private String message;

    @Builder
    public CreateWaitingRoomRes(Long id, String url, String message) {
        this.id = id;
        this.url = url;
        this.message = message;
    }
}

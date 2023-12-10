package com.rollingpaperserver.domain.waitingRoom.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CreateWaitingRoomRes {

    private String url;

    private String message;

    @Builder
    public CreateWaitingRoomRes(String url, String message) {
        this.url = url;
        this.message = message;
    }
}

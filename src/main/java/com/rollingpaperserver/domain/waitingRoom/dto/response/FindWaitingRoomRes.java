package com.rollingpaperserver.domain.waitingRoom.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class FindWaitingRoomRes {

    private String message;

    @Builder
    public FindWaitingRoomRes(String message) {
        this.message = message;
    }
}

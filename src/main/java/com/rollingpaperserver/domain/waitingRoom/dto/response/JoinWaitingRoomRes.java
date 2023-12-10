package com.rollingpaperserver.domain.waitingRoom.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class JoinWaitingRoomRes {

    private boolean canJoin;

    private String message;

    @Builder
    public JoinWaitingRoomRes(boolean canJoin, String message) {
        this.canJoin = canJoin;
        this.message = message;
    }
}

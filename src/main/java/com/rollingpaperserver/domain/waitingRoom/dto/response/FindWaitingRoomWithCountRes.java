package com.rollingpaperserver.domain.waitingRoom.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class FindWaitingRoomWithCountRes {

    private boolean canJoin;

    private Integer currentUserCount;

    private String message;

    @Builder
    public FindWaitingRoomWithCountRes(boolean canJoin, Integer currentUserCount, String message) {
        this.canJoin = canJoin;
        this.currentUserCount = currentUserCount;
        this.message = message;
    }
}

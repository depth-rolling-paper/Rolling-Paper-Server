package com.rollingpaperserver.domain.waitingRoom.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class FindWaitingRoomWithCountRes {

    private Integer currentUserCount;

    private String message;

    @Builder
    public FindWaitingRoomWithCountRes(Integer currentUserCount, String message) {
        this.currentUserCount = currentUserCount;
        this.message = message;
    }
}

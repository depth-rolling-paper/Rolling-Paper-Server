package com.rollingpaperserver.domain.waitingRoom.dto.response;

import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class FindWaitingRoomRes {

    private boolean canMake;

    private String message;

    @Builder
    public FindWaitingRoomRes(boolean canMake, String message) {
        this.canMake = canMake;
        this.message = message;
    }
}

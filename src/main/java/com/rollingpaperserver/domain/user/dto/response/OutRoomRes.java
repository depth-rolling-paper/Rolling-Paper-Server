package com.rollingpaperserver.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class OutRoomRes {

    private boolean emptyRoom;

    private Integer userCountInRoom;

    private String message;

    @Builder
    public OutRoomRes(boolean emptyRoom, Integer userCountInRoom, String message) {
        this.emptyRoom = emptyRoom;
        this.userCountInRoom = userCountInRoom;
        this.message = message;
    }

}

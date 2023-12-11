package com.rollingpaperserver.domain.room.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class DeleteRoomRes {

    private Long id;

    private String roomName;

    private String message;

    @Builder
    public DeleteRoomRes(Long id, String roomName, String message) {
        this.id = id;
        this.roomName = roomName;
        this.message = message;
    }
}

package com.rollingpaperserver.domain.room.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CreateRoomRes {

    private Long id;

    private String roomName;

    @Builder
    public CreateRoomRes(Long id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }

}

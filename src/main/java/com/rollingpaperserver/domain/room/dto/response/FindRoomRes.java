package com.rollingpaperserver.domain.room.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class FindRoomRes {

    private String message;

    @Builder
    public FindRoomRes(String message) {
        this.message = message;
    }
}

package com.rollingpaperserver.domain.room.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CreateRoomReq {

    /**
     * TODO : validation 처리 필요 ?
     *
     */

    private String roomName;
}

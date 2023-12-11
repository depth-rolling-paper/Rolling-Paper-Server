package com.rollingpaperserver.domain.room.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class DeleteRoomReq {

    private Long roomId;

    private String roomName;

}

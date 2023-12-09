package com.rollingpaperserver.domain.waitingRoom.dto.resquest;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class FindWaitingRoomUrlReq {

    private String url;
}

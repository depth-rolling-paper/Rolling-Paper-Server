package com.rollingpaperserver.domain.waitingRoom.dto.resquest;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CreateWaitingRoomReq {

    private String waiting_room_name;

    private int limit_user_num;

    private int start_time_minute;

}

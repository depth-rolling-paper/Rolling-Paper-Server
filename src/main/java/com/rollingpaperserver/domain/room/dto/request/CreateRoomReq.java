package com.rollingpaperserver.domain.room.dto.request;

import com.rollingpaperserver.domain.user.domain.User;
import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CreateRoomReq {

    private String waitingRoomUrl;

    private String roomName;

}

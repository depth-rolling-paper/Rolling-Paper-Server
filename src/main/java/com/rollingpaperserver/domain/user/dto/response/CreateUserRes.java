package com.rollingpaperserver.domain.user.dto.response;

import com.rollingpaperserver.domain.room.domain.Room;
import com.rollingpaperserver.domain.user.domain.UserType;
import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CreateUserRes {

    private Long id;
    private String userName;
    private UserType userType;
    private Room room;
    private WaitingRoom waitingRoom;

    @Builder
    public CreateUserRes(Long id, String userName, UserType userType, Room room, WaitingRoom waitingRoom) {
        this.id = id;
        this.userName = userName;
        this.userType = userType;
        this.room = room;
        this.waitingRoom = waitingRoom;
    }
}

package com.rollingpaperserver.domain.user.dto.request;

import com.rollingpaperserver.domain.user.domain.UserType;
import com.rollingpaperserver.domain.room.domain.Room;
import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CreateUserReq {

    private String userName;

    private UserType userType;

//    private Room room;
//    private Long roomId;

//    private WaitingRoom waitingRoom;
//    private Long waitingRoomId;

    @Builder
    public CreateUserReq(String userName, UserType userType) {
        this.userName = userName;
        this.userType = userType;
    }

}

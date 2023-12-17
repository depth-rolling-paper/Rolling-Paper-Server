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

    private int limitUserCount;

    private int currentUserCount;

    private String message;

    @Builder
    public CreateUserRes(Long id, int limitUserCount, int currentUserCount, String message) {
        this.id = id;
        this.limitUserCount = limitUserCount;
        this.currentUserCount = currentUserCount;
        this.message = message;
    }
}

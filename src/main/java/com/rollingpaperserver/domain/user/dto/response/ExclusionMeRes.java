package com.rollingpaperserver.domain.user.dto.response;

import com.rollingpaperserver.domain.room.domain.Room;
import com.rollingpaperserver.domain.user.domain.User;
import com.rollingpaperserver.domain.user.domain.UserType;
import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class ExclusionMeRes {

    private List<UserRes> users = new ArrayList<>();

    private String message;

    @Builder
    public ExclusionMeRes(List<UserRes> users, String message) {
        this.users = users;
        this.message = message;
    }
}

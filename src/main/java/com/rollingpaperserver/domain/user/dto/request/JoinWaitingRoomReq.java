package com.rollingpaperserver.domain.user.dto.request;

import com.rollingpaperserver.domain.user.domain.UserType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class JoinWaitingRoomReq {

    // user_id
    private Long id;

//    private UserType memberType;
}

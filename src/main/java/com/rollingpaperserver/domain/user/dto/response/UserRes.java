package com.rollingpaperserver.domain.user.dto.response;

import com.rollingpaperserver.domain.user.domain.UserType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class UserRes {

    private Long id;

    private String userName;

    private UserType userType;

    private Integer sequence;

    @Builder
    public UserRes(Long id, String userName, UserType userType, Integer sequence) {
        this.id = id;
        this.userName = userName;
        this.userType = userType;
        this.sequence = sequence;
    }
}

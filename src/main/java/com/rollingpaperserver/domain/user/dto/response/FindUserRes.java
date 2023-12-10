package com.rollingpaperserver.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class FindUserRes {

    private boolean canUse;

    private String message;

    @Builder
    public FindUserRes(boolean canUse, String message) {
        this.canUse = canUse;
        this.message = message;
    }
}

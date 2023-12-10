package com.rollingpaperserver.domain.rollingPaper.dto.response;

import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaperType;
import com.rollingpaperserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class RollingPaperRes {

    private Long rollingPaperId;

    private String message;

    @Builder
    public RollingPaperRes(Long rollingPaperId, String message) {
        this.rollingPaperId = rollingPaperId;
        this.message = message;
    }
}

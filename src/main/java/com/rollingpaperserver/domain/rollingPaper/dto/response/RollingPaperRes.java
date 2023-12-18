package com.rollingpaperserver.domain.rollingPaper.dto.response;

import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaperType;
import com.rollingpaperserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class RollingPaperRes {

//    private Long rollingPaperId;

    private List<Long> rollingPaperIds = new ArrayList<>();

    private String message;

//    @Builder
//    public RollingPaperRes(Long rollingPaperId, String message) {
//        this.rollingPaperId = rollingPaperId;
//        this.message = message;
//    }

    @Builder
    public RollingPaperRes(List<Long> rollingPaperIds, String message) {
        this.rollingPaperIds = rollingPaperIds;
        this.message = message;
    }
}

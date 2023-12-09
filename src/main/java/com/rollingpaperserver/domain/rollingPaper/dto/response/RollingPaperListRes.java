package com.rollingpaperserver.domain.rollingPaper.dto.response;

import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class RollingPaperListRes {

    private List<RollingPaper> rollingPapers = new ArrayList<>();

    private String message;

    @Builder
    public RollingPaperListRes(List<RollingPaper> rollingPapers, String message) {
        this.rollingPapers = rollingPapers;
        this.message = message;
    }
}

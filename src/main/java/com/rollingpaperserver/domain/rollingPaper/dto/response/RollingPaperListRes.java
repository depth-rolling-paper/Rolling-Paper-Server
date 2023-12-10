package com.rollingpaperserver.domain.rollingPaper.dto.response;

import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaper;
import com.rollingpaperserver.domain.rollingPaper.dto.ImageRollingPaperDTO;
import com.rollingpaperserver.domain.rollingPaper.dto.RollingPaperDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class RollingPaperListRes {

//    private List<RollingPaper> rollingPapers = new ArrayList<>();

    private List<RollingPaperDTO> rollingPapers = new ArrayList<>();

    private List<ImageRollingPaperDTO> imageRollingPapers = new ArrayList<>();

    private String message;

    @Builder
    public RollingPaperListRes(List<RollingPaperDTO> rollingPapers, List<ImageRollingPaperDTO> imageRollingPapers, String message) {
        this.rollingPapers = rollingPapers;
        this.imageRollingPapers = imageRollingPapers;
        this.message = message;
    }
}

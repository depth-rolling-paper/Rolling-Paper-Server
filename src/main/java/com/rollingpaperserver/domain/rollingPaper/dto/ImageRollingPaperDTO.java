package com.rollingpaperserver.domain.rollingPaper.dto;

import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaperType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class ImageRollingPaperDTO {

    private Long id;

    // Description : IMAGE, ROLLING_PAPER
    private RollingPaperType rollingPaperType;

    // Description : RollingPaperType = IMAGE
    private String imageName;

    private Double sizeX;

    private Double sizeY;

    @Builder
    public ImageRollingPaperDTO(Long id, RollingPaperType rollingPaperType, String imageName, Double sizeX, Double sizeY) {
        this.id = id;
        this.rollingPaperType = rollingPaperType;
        this.imageName = imageName;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
}

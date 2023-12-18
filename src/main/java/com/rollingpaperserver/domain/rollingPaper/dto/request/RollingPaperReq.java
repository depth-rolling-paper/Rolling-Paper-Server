package com.rollingpaperserver.domain.rollingPaper.dto.request;

import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaperType;
import com.rollingpaperserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class RollingPaperReq {

    // Description : RollingPaperType = ROLLING_PAPER

    @Nullable
    private Double location_x;

    @Nullable
    private Double location_y;

    @Nullable
    private Double rotation;

    @Nullable
    private Double width;

    @Nullable
    private Double height;

    @Nullable
    private Double scaleX;

    @Nullable
    private Double scaleY;

    @Nullable
    private String text;

    @Nullable
    private String fontFamily;

    // Description : IMAGE, ROLLING_PAPER
    private RollingPaperType rollingPaperType;

    // Description : RollingPaperType = IMAGE
    @Nullable
    private String imageName;

    @Nullable
    private Double sizeX;

    @Nullable
    private Double sizeY;

    private List<RollingPaperReq> rollingPaperList = new ArrayList<>();

}

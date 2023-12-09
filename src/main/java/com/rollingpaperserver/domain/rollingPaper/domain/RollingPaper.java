package com.rollingpaperserver.domain.rollingPaper.domain;

import com.rollingpaperserver.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RollingPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Description : RollingPaperType = ROLLING_PAPER
    private Double location_x;

    private Double location_y;

    private Double rotation;

    private Double width;

    private Double height;

    private Double scaleX;

    private Double scaleY;

    private String text;

    private String fontFamily;

    // Description : USER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    // Description : IMAGE, ROLLING_PAPER
    @Enumerated(EnumType.STRING)
    private RollingPaperType rollingPaperType;

    // Description : RollingPaperType = IMAGE
    private String imageName;

    private Double sizeX;

    private Double sizeY;

    @Builder
    public RollingPaper(Long id, Double location_x, Double location_y, Double rotation, Double width, Double height, Double scaleX, Double scaleY, String text, String fontFamily, User user, RollingPaperType rollingPaperType, String imageName, Double sizeX, Double sizeY) {
        this.id = id;
        this.location_x = location_x;
        this.location_y = location_y;
        this.rotation = rotation;
        this.width = width;
        this.height = height;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.text = text;
        this.fontFamily = fontFamily;
        this.user = user;
        this.rollingPaperType = rollingPaperType;
        this.imageName = imageName;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
}

package com.rollingpaperserver.domain.rollingPaper.application;

import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaper;
import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaperType;
import com.rollingpaperserver.domain.rollingPaper.domain.repository.RollingPaperRepository;
import com.rollingpaperserver.domain.rollingPaper.dto.request.RollingPaperReq;
import com.rollingpaperserver.domain.rollingPaper.dto.response.RollingPaperListRes;
import com.rollingpaperserver.domain.rollingPaper.dto.response.RollingPaperRes;
import com.rollingpaperserver.domain.user.domain.User;
import com.rollingpaperserver.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RollingPaperService {

    private final RollingPaperRepository rollingPaperRepository;
    private final UserRepository userRepository;

    // Description : 유저의 롤링페이퍼 목록 조회
    public ResponseEntity<?> findRollingPaperList(Long userId) {

        List<RollingPaper> rollingPaperList = rollingPaperRepository.findAllRollingPaperWithUser(userId);

        RollingPaperListRes rollingPaperListRes = RollingPaperListRes.builder()
                .rollingPapers(rollingPaperList)
                .message("해당 유저의 롤링페이퍼 목록입니다.")
                .build();

        return ResponseEntity.ok(rollingPaperListRes);

    }

    // Description : 롤링페이퍼 작성
    // TODO : Sender(Name) 고민
    @Transactional
    public ResponseEntity<?> writeRollingPaper(Long userId, RollingPaperReq rollingPaperReq) {

        Optional<User> userWithRoomAndWaitingRoom = userRepository.findUserWithRoomAndWaitingRoom(userId);

        if (!userWithRoomAndWaitingRoom.isPresent())
            return ResponseEntity.badRequest().body("사용자가 존재하지 않습니다.");

        User user = userWithRoomAndWaitingRoom.get();

        RollingPaper rollingPaper =  null;

        // Description : RollingPaperType == ROLLING_PAPER
        if (rollingPaperReq.getRollingPaperType() == RollingPaperType.ROLLING_PAPER) {
            rollingPaper = RollingPaper.builder()
                    .rollingPaperType(rollingPaperReq.getRollingPaperType())
                    .location_x(rollingPaperReq.getLocation_x())
                    .location_y(rollingPaperReq.getLocation_y())
                    .rotation(rollingPaperReq.getRotation())
                    .width(rollingPaperReq.getWidth())
                    .height(rollingPaperReq.getHeight())
                    .scaleX(rollingPaperReq.getScaleX())
                    .scaleY(rollingPaperReq.getScaleY())
                    .text(rollingPaperReq.getText())
                    .fontFamily(rollingPaperReq.getFontFamily())
                    .user(user)
                    .build();

        } else { // Description : RollingPaperType == IMAGE
            rollingPaper = RollingPaper.builder()
                    .rollingPaperType(rollingPaperReq.getRollingPaperType())
                    .imageName(rollingPaperReq.getImageName())
                    .sizeX(rollingPaperReq.getSizeX())
                    .sizeY(rollingPaperReq.getSizeY())
                    .user(user)
                    .build();

        }

        rollingPaperRepository.save(rollingPaper);

        RollingPaperRes rollingPaperRes = RollingPaperRes.builder()
                .rollingPaperId(rollingPaper.getId())
                .message("롤링페이퍼가 작성되었습니다.")
                .build();

        return ResponseEntity.ok(rollingPaperRes);

    }

}

package com.rollingpaperserver.domain.rollingPaper.application;

import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaper;
import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaperType;
import com.rollingpaperserver.domain.rollingPaper.domain.repository.RollingPaperRepository;
import com.rollingpaperserver.domain.rollingPaper.dto.ImageRollingPaperDTO;
import com.rollingpaperserver.domain.rollingPaper.dto.RollingPaperDTO;
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
        List<ImageRollingPaperDTO> imageDtos = new ArrayList<>();
        List<RollingPaperDTO> dtos = new ArrayList<>();

        for (RollingPaper rollingPaper : rollingPaperList) {
            if (rollingPaper.getRollingPaperType().equals(RollingPaperType.IMAGE)) {
                ImageRollingPaperDTO imageRollingPaperDTO = ImageRollingPaperDTO.builder()
                        .id(rollingPaper.getId())
                        .rollingPaperType(rollingPaper.getRollingPaperType())
                        .imageName(rollingPaper.getImageName())
                        .sizeX(rollingPaper.getSizeX())
                        .sizeY(rollingPaper.getSizeY())
                        .build();

                imageDtos.add(imageRollingPaperDTO);

            } else {
                // If : TYPE = "ROLLING_PAPER"
                RollingPaperDTO rollingPaperDTO = RollingPaperDTO.builder()
                        .id(rollingPaper.getId())
                        .location_x(rollingPaper.getLocation_x())
                        .location_y(rollingPaper.getLocation_y())
                        .rotation(rollingPaper.getRotation())
                        .width(rollingPaper.getWidth())
                        .height(rollingPaper.getHeight())
                        .scaleX(rollingPaper.getScaleX())
                        .scaleY(rollingPaper.getScaleY())
                        .text(rollingPaper.getText())
                        .fontFamily(rollingPaper.getFontFamily())
                        .rollingPaperType(rollingPaper.getRollingPaperType())
                        .build();

                dtos.add(rollingPaperDTO);
            }
        }

        RollingPaperListRes rollingPaperListRes = RollingPaperListRes.builder()
                .rollingPapers(dtos)
                .imageRollingPapers(imageDtos)
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

//        RollingPaper rollingPaper =  null;
        List<Long> ids = new ArrayList<>();

        List<RollingPaperReq> rollingPaperList = rollingPaperReq.getRollingPaperList();
        for (RollingPaperReq paperReq : rollingPaperList) {

            // Description : RollingPaperType == ROLLING_PAPER
            if (paperReq.getRollingPaperType() == RollingPaperType.ROLLING_PAPER) {
                RollingPaper rollingPaper = RollingPaper.builder()
                        .rollingPaperType(paperReq.getRollingPaperType())
                        .location_x(paperReq.getLocation_x())
                        .location_y(paperReq.getLocation_y())
                        .rotation(paperReq.getRotation())
                        .width(paperReq.getWidth())
                        .height(paperReq.getHeight())
                        .scaleX(paperReq.getScaleX())
                        .scaleY(paperReq.getScaleY())
                        .text(paperReq.getText())
                        .fontFamily(paperReq.getFontFamily())
                        .user(user)
                        .build();

                rollingPaperRepository.save(rollingPaper);
                ids.add(rollingPaper.getId());

            } else { // Description : RollingPaperType == IMAGE

                RollingPaper rollingPaper = RollingPaper.builder()
                        .rollingPaperType(paperReq.getRollingPaperType())
                        .imageName(paperReq.getImageName())
                        .sizeX(paperReq.getSizeX())
                        .sizeY(paperReq.getSizeY())
                        .user(user)
                        .build();

                rollingPaperRepository.save(rollingPaper);
                ids.add(rollingPaper.getId());

            }

        }

        RollingPaperRes rollingPaperRes = RollingPaperRes.builder()
                .rollingPaperIds(ids)
                .message("롤링페이퍼가 작성되었습니다.")
                .build();

        return ResponseEntity.ok(rollingPaperRes);


//        // Description : RollingPaperType == ROLLING_PAPER
//        if (rollingPaperReq.getRollingPaperType() == RollingPaperType.ROLLING_PAPER) {
//            rollingPaper = RollingPaper.builder()
//                    .rollingPaperType(rollingPaperReq.getRollingPaperType())
//                    .location_x(rollingPaperReq.getLocation_x())
//                    .location_y(rollingPaperReq.getLocation_y())
//                    .rotation(rollingPaperReq.getRotation())
//                    .width(rollingPaperReq.getWidth())
//                    .height(rollingPaperReq.getHeight())
//                    .scaleX(rollingPaperReq.getScaleX())
//                    .scaleY(rollingPaperReq.getScaleY())
//                    .text(rollingPaperReq.getText())
//                    .fontFamily(rollingPaperReq.getFontFamily())
//                    .user(user)
//                    .build();
//
//        } else { // Description : RollingPaperType == IMAGE
//            rollingPaper = RollingPaper.builder()
//                    .rollingPaperType(rollingPaperReq.getRollingPaperType())
//                    .imageName(rollingPaperReq.getImageName())
//                    .sizeX(rollingPaperReq.getSizeX())
//                    .sizeY(rollingPaperReq.getSizeY())
//                    .user(user)
//                    .build();
//
//        }

//        rollingPaperRepository.save(rollingPaper);

//        RollingPaperRes rollingPaperRes = RollingPaperRes.builder()
//                .rollingPaperId(rollingPaper.getId())
//                .message("롤링페이퍼가 작성되었습니다.")
//                .build();
//
//        return ResponseEntity.ok(rollingPaperRes);

    }

}

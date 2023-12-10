package com.rollingpaperserver.domain.rollingPaper.presentation;

import com.rollingpaperserver.domain.rollingPaper.application.RollingPaperService;
import com.rollingpaperserver.domain.rollingPaper.dto.request.RollingPaperReq;
import com.rollingpaperserver.domain.rollingPaper.dto.response.RollingPaperListRes;
import com.rollingpaperserver.domain.rollingPaper.dto.response.RollingPaperRes;
import com.rollingpaperserver.domain.room.dto.response.CreateRoomRes;
import com.rollingpaperserver.domain.waitingRoom.dto.response.FindWaitingRoomRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rolling-Paper API", description = "Rolling-Paper 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rolling-papers")
public class RollingPaperController {

    private final RollingPaperService rollingPaperService;

    // Description : 유저의 롤링페이퍼 목록 조회
    // TODO : OK
    @Operation(summary = "롤링페이퍼 목록 조회", description = "user-id를 통해 유저의 롤링페이퍼 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "롤링페이퍼 목록 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RollingPaperListRes.class))}),
            @ApiResponse(responseCode = "400", description = "롤링페이퍼 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{user-id}")
    public ResponseEntity<?> findRollingPaperList(@PathVariable(value = "user-id") Long userId) {
        return rollingPaperService.findRollingPaperList(userId);

    }

    // Description : 해당 유저에게 롤링페이퍼 작성
    // TODO : OK / sender 생각
    @Operation(summary = "롤링페이퍼 작성", description = "user-id를 가진 유저에게 롤링페이퍼를 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "롤링페이퍼 작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RollingPaperRes.class))}),
            @ApiResponse(responseCode = "400", description = "롤링페이퍼 작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/{user-id}")
    public ResponseEntity<?> writeRollingPaper(@PathVariable(value = "user-id") Long userId, @RequestBody RollingPaperReq rollingPaperReq) {
        return rollingPaperService.writeRollingPaper(userId, rollingPaperReq);

    }
}

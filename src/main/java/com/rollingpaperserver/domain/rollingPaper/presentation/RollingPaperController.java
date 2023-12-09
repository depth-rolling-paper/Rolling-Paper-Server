package com.rollingpaperserver.domain.rollingPaper.presentation;

import com.rollingpaperserver.domain.rollingPaper.application.RollingPaperService;
import com.rollingpaperserver.domain.rollingPaper.dto.request.RollingPaperReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rolling-papers")
public class RollingPaperController {

    private final RollingPaperService rollingPaperService;

    // Description : 유저의 롤링페이퍼 목록 조회
    @GetMapping("/{user-id}")
    public ResponseEntity<?> findRollingPaperList(@PathVariable(value = "user-id") Long userId) {
        return rollingPaperService.findRollingPaperList(userId);

    }

    // Description : 롤링페이퍼 작성
    @PostMapping("/{user-id}")
    public ResponseEntity<?> writeRollingPaper(@PathVariable(value = "user-id") Long userId, @RequestBody RollingPaperReq rollingPaperReq) {
        return rollingPaperService.writeRollingPaper(userId, rollingPaperReq);

    }
}

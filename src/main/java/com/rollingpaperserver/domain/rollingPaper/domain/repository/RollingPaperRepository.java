package com.rollingpaperserver.domain.rollingPaper.domain.repository;

import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaper;
import com.rollingpaperserver.domain.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RollingPaperRepository extends JpaRepository<RollingPaper, Long> {

    @Query("SELECT DISTINCT r FROM RollingPaper r " +
            "LEFT JOIN FETCH r.user u " +
            "LEFT JOIN FETCH u.room " +
            "LEFT JOIN FETCH u.waitingRoom " +
            "WHERE u.id = :userId")
    List<RollingPaper> findAllRollingPaperWithUser(@Param("userId") Long userId);


}

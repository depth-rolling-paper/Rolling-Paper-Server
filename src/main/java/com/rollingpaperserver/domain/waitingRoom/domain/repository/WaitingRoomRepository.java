package com.rollingpaperserver.domain.waitingRoom.domain.repository;

import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
public interface WaitingRoomRepository extends JpaRepository<WaitingRoom, Long> {

    Optional<WaitingRoom> findByUrl(String url);

    Optional<WaitingRoom> findByWaitingRoomName(String waitingRoomName);

}

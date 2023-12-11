package com.rollingpaperserver.domain.room.domain.repository;

import com.rollingpaperserver.domain.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomName(String roomName);
}

package com.rollingpaperserver.domain.room.domain.repository;

import com.rollingpaperserver.domain.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}

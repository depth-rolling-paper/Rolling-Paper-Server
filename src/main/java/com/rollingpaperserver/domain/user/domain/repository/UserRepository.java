package com.rollingpaperserver.domain.user.domain.repository;

import com.rollingpaperserver.domain.user.domain.User;
import com.rollingpaperserver.domain.user.dto.UserDTO;
import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    // DTO와 함께 사용
    List<User> findAllByWaitingRoom(WaitingRoom waitingRoom);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.room LEFT JOIN FETCH u.waitingRoom")
    List<User> findAllWithRoomAndWaitingRoom();

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.room LEFT JOIN FETCH u.waitingRoom WHERE u.id = :userId")
    Optional<User> findUserWithRoomAndWaitingRoom(@Param("userId") Long userId);


}

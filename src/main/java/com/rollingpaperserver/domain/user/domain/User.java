package com.rollingpaperserver.domain.user.domain;

import com.rollingpaperserver.domain.room.domain.Room;
import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private WaitingRoom waitingRoom;

    public void updateWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
    }

    @Builder
    public User(Long id, String userName, UserType userType, Room room, WaitingRoom waitingRoom) {
        this.id = id;
        this.userName = userName;
        this.userType = userType;
        this.room = room;
        this.waitingRoom = waitingRoom;
    }
}

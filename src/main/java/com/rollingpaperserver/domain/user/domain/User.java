package com.rollingpaperserver.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rollingpaperserver.domain.rollingPaper.domain.RollingPaper;
import com.rollingpaperserver.domain.room.domain.Room;
import com.rollingpaperserver.domain.waitingRoom.domain.WaitingRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @JsonBackReference
    private WaitingRoom waitingRoom;

    @OneToMany(mappedBy = "user")
    private List<RollingPaper> rollingPapers = new ArrayList<>();

    // Description : 대기 방 입장 순서
    private Integer sequence;

    public void updateWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
    }

    public void updateRoom(Room room) {
        this.room = room;
    }

    @Builder
    public User(Long id, String userName, UserType userType, Room room, WaitingRoom waitingRoom, Integer sequence) {
        this.id = id;
        this.userName = userName;
        this.userType = userType;
        this.room = room;
        this.waitingRoom = waitingRoom;
        this.sequence = sequence;
    }

}

package com.rollingpaperserver.domain.room.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rollingpaperserver.domain.user.domain.User;
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
public class Room {
    
    // TODO : Room에 mappedBy 설정 고려, 총 인원 수 설정 고려하기

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    @OneToMany(mappedBy = "room")
    @JsonManagedReference
    private List<User> users = new ArrayList<>();

    @Builder
    public Room(Long id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }
}

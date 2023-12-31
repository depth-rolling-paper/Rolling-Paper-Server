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
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    private String url;

    @OneToMany(mappedBy = "room")
    private List<User> users = new ArrayList<>();

    @Builder
    public Room(Long id, String roomName, String url) {
        this.id = id;
        this.roomName = roomName;
        this.url = url;
    }

}

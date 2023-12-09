package com.rollingpaperserver.domain.room.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Room {
    
    // TODO : Room에 mappedBy 설정 고려, 총 인원 수 설정 고려하기

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    @Builder
    public Room(Long id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }
}

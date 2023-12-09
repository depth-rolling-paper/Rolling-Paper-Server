package com.rollingpaperserver.domain.waitingRoom.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class WaitingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String waiting_room_name;

    private int limit_user_num;

    private int current_user_num;

    private LocalDateTime start_time;

    private String url;

    // Description : Update Method
    public void updateCurrentUserNum(int current_user_num) {
        this.current_user_num = current_user_num;
    }

    @Builder
    public WaitingRoom(Long id, String waiting_room_name, int limit_user_num, int current_user_num, LocalDateTime start_time, String url) {
        this.id = id;
        this.waiting_room_name = waiting_room_name;
        this.limit_user_num = limit_user_num;
        this.current_user_num = current_user_num;
        this.start_time = start_time;
        this.url = url;
    }
}

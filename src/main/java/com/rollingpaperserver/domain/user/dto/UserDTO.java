package com.rollingpaperserver.domain.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class UserDTO {

    private Long id;

    private String userName;

    @Builder
    public UserDTO(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}

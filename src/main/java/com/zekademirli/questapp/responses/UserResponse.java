package com.zekademirli.questapp.responses;

import com.zekademirli.questapp.entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private Long avatarId;
    private String username;

    public UserResponse(User user) {
        this.id = user.getId();
        this.avatarId= user.getAvatar();
        this.username = user.getUsername();
    }

}

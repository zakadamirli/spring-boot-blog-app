package com.zekademirli.questapp.responses;

import com.zekademirli.questapp.entities.Like;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeResponse {
    private Long id;
    private Long userId;
    private Long postId;

    public LikeResponse(Like like) {
        this.id = like.getId();
        this.userId = like.getUser().getId();
        this.postId = like.getPost().getId();
    }
}

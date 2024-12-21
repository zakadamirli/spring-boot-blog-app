package com.zekademirli.questapp.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeCreateRequest {
    private Long id;
    private Long userId;
    private Long postId;
}

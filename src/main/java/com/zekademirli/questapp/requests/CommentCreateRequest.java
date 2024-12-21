package com.zekademirli.questapp.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequest {

    private Long id;
    private Long userId;
    private Long postId;
    private String text;
}

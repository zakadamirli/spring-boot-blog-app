package com.zekademirli.questapp.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateRequest {

    private String text;
    private String title;
    private Long userId;
}

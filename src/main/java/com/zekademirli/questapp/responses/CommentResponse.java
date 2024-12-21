package com.zekademirli.questapp.responses;

import com.zekademirli.questapp.entities.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {

    private Long id;
    private Long userId;
    private String text;
    private String userName;

    public CommentResponse(Comment entity) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.text = entity.getText();
        this.userName = entity.getUser().getUsername();
    }
}
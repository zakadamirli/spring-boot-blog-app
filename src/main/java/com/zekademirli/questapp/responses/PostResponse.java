package com.zekademirli.questapp.responses;

import com.zekademirli.questapp.entities.Like;
import com.zekademirli.questapp.entities.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String text;
    private List<LikeResponse> postLikes;

    public PostResponse(Post entity, List<LikeResponse> postLikes) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getUsername();
        this.title = entity.getTitle();
        this.text = entity.getText();
        this.postLikes = postLikes;
    }
}

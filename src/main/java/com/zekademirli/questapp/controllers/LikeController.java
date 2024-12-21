package com.zekademirli.questapp.controllers;

import com.zekademirli.questapp.entities.Like;
import com.zekademirli.questapp.requests.LikeCreateRequest;
import com.zekademirli.questapp.responses.LikeResponse;
import com.zekademirli.questapp.services.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/likes")
@AllArgsConstructor
public class LikeController {
    private LikeService likeService;

    @GetMapping
    public List<LikeResponse> getAllLikes(Optional<Long> userId, Optional<Long> postId) {
        return likeService.getAllLikes(userId, postId);
    }

    @GetMapping("/{likeId}")
    public Like getOneLike(@PathVariable Long likeId) {
        return likeService.getOneLikeById(likeId);
    }

    @PostMapping
    public Like createOneLike(@RequestBody LikeCreateRequest likeCreateRequest) {
        return likeService.createOneLike(likeCreateRequest);
    }

    @DeleteMapping("/{likeId}")
    public void deleteOneLike(@PathVariable Long likeId) {
        likeService.deleteOneLikeById(likeId);
    }
}

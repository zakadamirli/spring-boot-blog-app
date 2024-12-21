package com.zekademirli.questapp.controllers;

import com.zekademirli.questapp.entities.Post;
import com.zekademirli.questapp.requests.PostCreateRequest;
import com.zekademirli.questapp.requests.PostUpdateRequest;
import com.zekademirli.questapp.responses.PostResponse;
import com.zekademirli.questapp.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private PostService postService;

    @GetMapping
    public List<PostResponse> getPosts(@RequestParam(required = false) Optional<Long> userId) {
        return postService.getAllPosts(userId);
    }

    @GetMapping("/{postId}")
    public PostResponse getOnePost(@PathVariable Long postId) {
        return postService.getOnePostByIdWithLikes(postId);
    }

    @PostMapping
    public Post createOnePost(@RequestBody PostCreateRequest newPostRequest) {
        return postService.createOnePost(newPostRequest);
    }

    @PutMapping("/{postId}")
    public Post updateOnePost(@PathVariable Long postId, @RequestBody PostUpdateRequest newPostRequest) {
        return postService.updateOnePostById(postId, newPostRequest);
    }

    @DeleteMapping("/{postId}")
    public void deleteOnePost(@PathVariable Long postId) {
        postService.deleteOnePostById(postId);
    }
}

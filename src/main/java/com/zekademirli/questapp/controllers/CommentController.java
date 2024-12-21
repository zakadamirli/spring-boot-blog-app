package com.zekademirli.questapp.controllers;

import com.zekademirli.questapp.entities.Comment;
import com.zekademirli.questapp.requests.CommentCreateRequest;
import com.zekademirli.questapp.requests.CommentUpdateRequest;
import com.zekademirli.questapp.responses.CommentResponse;
import com.zekademirli.questapp.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentResponse> getAllComments(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> postId) {
        return commentService.getAllCommentsWithParams(userId,postId);
    }

    @GetMapping("/{commentId}")
    public Comment getOneComment(@PathVariable Long commentId) {
        return commentService.getOneCommentById(commentId);
    }


    @PostMapping
    public Comment addComment(@RequestBody CommentCreateRequest commentCreateRequest) {
        return commentService.createOneComment(commentCreateRequest);
    }

    @PutMapping("/{commentId}")
    public Comment updateOneComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {
        return commentService.updateOneCommentById(commentId, request);
    }

    @DeleteMapping("{commentId}")
    public void deleteOneComment(@PathVariable Long commentId) {
        commentService.deleteOneCommentById(commentId);
    }
}

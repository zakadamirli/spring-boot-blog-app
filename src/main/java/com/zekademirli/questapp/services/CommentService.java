package com.zekademirli.questapp.services;

import com.zekademirli.questapp.entities.Comment;
import com.zekademirli.questapp.entities.Post;
import com.zekademirli.questapp.entities.User;
import com.zekademirli.questapp.repository.CommentRepository;
import com.zekademirli.questapp.requests.CommentCreateRequest;
import com.zekademirli.questapp.requests.CommentUpdateRequest;
import com.zekademirli.questapp.responses.CommentResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;
    private UserService userService;
    private PostService postService;

    @Transactional
    public List<CommentResponse> getAllCommentsWithParams(Optional<Long> userId, Optional<Long> postId) {
        List<Comment> comments;
        if (userId.isPresent() && postId.isPresent()) {
            comments = commentRepository.findByUserIdAndPostId(userId.get(), postId.get());
        } else if (userId.isPresent()) {
            comments = commentRepository.findByUserId(userId.get());
        } else if (postId.isPresent()) {
            comments = commentRepository.findByPostId(postId.get());
        } else comments = commentRepository.findAll();
        return comments.stream().map(CommentResponse::new).toList();
    }

    @Transactional
    public Comment createOneComment(CommentCreateRequest commentCreateRequest) {
        User user = userService.getOneUserById(commentCreateRequest.getUserId());
        Post post = postService.getOnePostById(commentCreateRequest.getPostId());
        if (user != null && post != null) {
            Comment commentToSave = new Comment();
            commentToSave.setId(commentCreateRequest.getId());
            commentToSave.setUser(user);
            commentToSave.setPost(post);
            commentToSave.setText(commentCreateRequest.getText());
            commentToSave.setCreateDate(new Date());
            return commentRepository.save(commentToSave);
        } else return null;
    }

    @Transactional
    public Comment updateOneCommentById(Long commentId, CommentUpdateRequest request) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            Comment commentToUpdate = comment.get();
            commentToUpdate.setText(request.getText());
            return commentRepository.save(commentToUpdate);
        } else return null;
    }

    @Transactional
    public void deleteOneCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Comment getOneCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }
}

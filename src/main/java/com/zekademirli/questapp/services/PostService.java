package com.zekademirli.questapp.services;

import com.zekademirli.questapp.entities.Post;
import com.zekademirli.questapp.entities.User;
import com.zekademirli.questapp.repository.PostRepository;
import com.zekademirli.questapp.requests.PostCreateRequest;
import com.zekademirli.questapp.requests.PostUpdateRequest;
import com.zekademirli.questapp.responses.LikeResponse;
import com.zekademirli.questapp.responses.PostResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostService {

    private final UserService userService;
    private final PostRepository postRepository;
    private final LikeService likeService;

    public PostService(UserService userService, PostRepository postRepository, @Lazy LikeService likeService) {
        this.userService = userService;
        this.postRepository = postRepository;
        this.likeService = likeService;
    }

    @Transactional
    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        List<Post> list;
        if (userId.isPresent()) {
            list = postRepository.findByUserId(userId.get());
        } else {
            list = postRepository.findAll();
        }

        return list.stream().map(p -> {
            List<LikeResponse> likes = likeService.getAllLikes(Optional.empty(), Optional.of(p.getId()));
            return new PostResponse(p, likes);
        }).toList();
    }

    @Transactional
    public Post getOnePostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    @Transactional
    public PostResponse getOnePostByIdWithLikes(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<LikeResponse> likes = likeService.getAllLikes(Optional.ofNullable(null), Optional.of(postId));
        assert post != null;
        return new PostResponse(post, likes);
    }

    @Transactional
    public Post createOnePost(PostCreateRequest newCreateRequest) {
        User user = userService.getOneUserById(newCreateRequest.getUserId());
        if (user == null)
            return null;
        Post toSave = new Post();
        toSave.setTitle(newCreateRequest.getTitle());
        toSave.setText(newCreateRequest.getText());
        toSave.setUser(user);
        toSave.setCreateDate(new Date());
        log.info("save post");
        return postRepository.save(toSave);
    }

    @Transactional
    public Post updateOnePostById(Long postId, PostUpdateRequest newPostRequest) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            Post postToUpdate = post.get();
            postToUpdate.setText(newPostRequest.getText());
            postToUpdate.setTitle(newPostRequest.getTitle());
            return postRepository.save(postToUpdate);
        }
        return null;
    }

    @Transactional
    public void deleteOnePostById(Long postId) {
        postRepository.deleteById(postId);
    }
}
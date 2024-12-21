package com.zekademirli.questapp.services;

import com.zekademirli.questapp.entities.Like;
import com.zekademirli.questapp.entities.Post;
import com.zekademirli.questapp.entities.User;
import com.zekademirli.questapp.repository.LikeRepository;
import com.zekademirli.questapp.requests.LikeCreateRequest;
import com.zekademirli.questapp.responses.LikeResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    @Transactional
    public List<LikeResponse> getAllLikes(Optional<Long> userId, Optional<Long> postId) {
        List<Like> likes;
        if (userId.isPresent() && postId.isPresent()) {
            likes = likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
        } else if (userId.isPresent()) {
            likes = likeRepository.findByUserId(userId.get());
        } else if (postId.isPresent()) {
            likes = likeRepository.findByPostId(postId.get());
        } else {
            likes = likeRepository.findAll();
        }
        return likes.stream().map(LikeResponse::new).toList();
    }

    @Transactional
    public Like getOneLikeById(Long likeId) {
        return likeRepository.findById(likeId).orElse(null);
    }

    @Transactional
    public Like createOneLike(LikeCreateRequest likeCreateRequest) {
        User user = userService.getOneUserById(likeCreateRequest.getUserId());
        Post post = postService.getOnePostById(likeCreateRequest.getPostId());
        if (post != null && user != null) {
            Like likeToSave = new Like();
            likeToSave.setUser(user);
            likeToSave.setPost(post);
            likeToSave.setId(likeCreateRequest.getId());
            return likeRepository.save(likeToSave);
        }
        return null;
    }

    @Transactional
    public void deleteOneLikeById(Long likeId) {
        likeRepository.deleteById(likeId);
    }
}
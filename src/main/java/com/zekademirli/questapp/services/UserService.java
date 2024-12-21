package com.zekademirli.questapp.services;

import com.zekademirli.questapp.entities.User;
import com.zekademirli.questapp.repository.CommentRepository;
import com.zekademirli.questapp.repository.LikeRepository;
import com.zekademirli.questapp.repository.PostRepository;
import com.zekademirli.questapp.repository.UserRepository;
import com.zekademirli.questapp.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserResponse::new).toList();
    }

    public User saveOneUser(User user) {
        return userRepository.save(user);
    }

    public User getOneUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User updateOneUser(User user, Long userId) {
        Optional<User> updatedUser = userRepository.findById(userId);
        if (updatedUser.isPresent()) {
            User existingUser = updatedUser.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setAvatar(user.getAvatar());
            return userRepository.save(existingUser);
        } else
            return null;
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User getOneUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public List<Object> getUserActivity(Long userId) {
        List<Long> postIds = postRepository.findTopByUserId(userId);
        if (postIds.isEmpty()) {
            return null;
        }
        List<Object> comments = commentRepository.findUserCommentsByPostId(postIds);
        List<Object> likes = likeRepository.findUserLikesByPostId(postIds);
        List<Object> objects = new ArrayList<>();
        objects.addAll(comments);
        objects.addAll(likes);
        return objects;
    }
}
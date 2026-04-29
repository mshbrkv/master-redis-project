package org.example.postservice.service.impl;

import lombok.AllArgsConstructor;
import org.example.postservice.model.Post;
import org.example.postservice.model.PostLike;
import org.example.postservice.repository.PostLikeRepository;
import org.example.postservice.repository.PostRepository;
import org.example.postservice.service.KafkaProducerService;
import org.example.postservice.service.PostService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final KafkaProducerService kafkaProducerService;


    @Override
    public Post createPost(final Post post) {

        Post savedPost = postRepository.save(post);
        kafkaProducerService.sendPostMessage(savedPost.getAuthorId());
        return savedPost;
    }

    @Override
    public void likePost(final UUID postId, final UUID userId) {

        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post with id " + postId + " does not exist");
        }

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new IllegalStateException("Post with id " + postId + " already liked by user " + userId);
        }

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);
        kafkaProducerService.sendLikeMessage(userId);
    }
}

package org.example.postservice.service;

import org.example.postservice.dto.PostCreationDto;
import org.example.postservice.model.Post;

import java.util.UUID;

public interface PostService {

    Post createPost(Post post);
    void likePost(UUID postId, UUID userId) throws Exception;
}

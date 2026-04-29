package org.example.postservice.repository;

import org.example.postservice.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {

    boolean existsByPostIdAndUserId(UUID postId, UUID userId);

}

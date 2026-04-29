package org.example.postservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.postservice.dto.PostCreationDto;
import org.example.postservice.model.Post;
import org.example.postservice.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<Post> create(@RequestBody PostCreationDto dto) {

        Post post = new Post();
        post.setAuthorId(dto.getAuthorId());
        post.setContent(dto.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(post));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> like(@PathVariable UUID postId,
                                       @RequestParam UUID userId) throws Exception {

        try {
            postService.likePost(postId, userId);
            return ResponseEntity.ok("Liked");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error" + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error" + e.getMessage());
        }
    }
}

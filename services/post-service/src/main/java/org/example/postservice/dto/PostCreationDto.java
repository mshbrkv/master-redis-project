package org.example.postservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PostCreationDto {

    private UUID authorId;
    private String content;
}

package org.example.leaderboardservice.dto;

import lombok.Data;

@Data
public class ActionMessage {

    private String eventId;
    private String userId;
    private String action;
    private int points;
    private String timestamp;
}

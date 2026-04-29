package org.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionMessage {

    private String eventId;
    private String userId;
    private String action;
    private int points;
    private String timestamp;

}

package org.example.leaderboardservice.listener;

import lombok.RequiredArgsConstructor;
import org.example.leaderboardservice.dto.ActionMessage;
import org.example.leaderboardservice.service.LeaderboardService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final ObjectMapper objectMapper;
    private final LeaderboardService leaderboardService;

    @KafkaListener(topics = "user-events", groupId = "leaderboard-service-group")
    public void consumerUserEvent(String message) {

        try {
            System.out.println("Received message: " + message);
            ActionMessage event = objectMapper.readValue(message, ActionMessage.class);
            leaderboardService.addPoints(event.getEventId(), event.getUserId(), event.getPoints());
        } catch (Exception e) {
            System.out.println("Error while processing kafka message: " + e.getMessage());
        }
    }
}

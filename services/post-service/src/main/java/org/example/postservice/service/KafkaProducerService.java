package org.example.postservice.service;

import lombok.RequiredArgsConstructor;
import org.example.postservice.dto.ActionMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC = "user-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendPostMessage(UUID userId) {

        try {
            ActionMessage message = new ActionMessage(
                    UUID.randomUUID().toString(),
                    userId.toString(),
                    "CREATE_POST",
                    15,
                    LocalDateTime.now().toString()
            );
            String json = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(TOPIC, json);
            System.out.println("Message sent to topic " + json);
        } catch (Exception e) {
            System.out.println("Error sending message to topic " + e.getMessage());
        }
    }

    public void sendLikeMessage(UUID userId) {

        try {
            ActionMessage message = new ActionMessage(
                    UUID.randomUUID().toString(),
                    userId.toString(),
                    "LIKE_POST",
                    5,
                    LocalDateTime.now().toString()
            );
            String json = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(TOPIC, json);
            System.out.println("Message sent to topic " + json);

        }catch (Exception e) {
            System.out.println("Error sending message to topic " + e.getMessage());
        }

    }
}

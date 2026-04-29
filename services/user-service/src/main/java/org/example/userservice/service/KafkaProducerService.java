package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.ActionMessage;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void sendMessage(UUID userId) {

        try {
            ActionMessage message = new ActionMessage(
                    UUID.randomUUID().toString(),
                    userId.toString(),
                    "REGISTRATION",
                    10,
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

package org.example.leaderboardservice.service;


import lombok.RequiredArgsConstructor;
import org.example.leaderboardservice.dto.LeaderboardEntryDto;
import org.example.leaderboardservice.model.LeaderboardRecord;
import org.example.leaderboardservice.repository.LeaderboardRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private static final String LEADERBOARD_KEY = "leaderboard";
    private static final String PROCESSED_EVENT_PREFIX = "processed_event:";
    private static final int NUMBER_TOP_USERS = 10;
    private final StringRedisTemplate redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final LeaderboardRepository sqlRepository;

    @Transactional
    public void addPoints(String eventId, String userId, int points) {

        String eventKy = PROCESSED_EVENT_PREFIX + eventId;

        Boolean isNewEvent = redisTemplate.opsForValue().setIfAbsent(eventKy, "DONE", Duration.ofHours(24));


        if (Boolean.TRUE.equals(isNewEvent)) {

            //REDIS
            redisTemplate.opsForZSet().incrementScore(LEADERBOARD_KEY, userId, points);

            //SQL
            LeaderboardRecord record = sqlRepository.findById(userId).orElse(new LeaderboardRecord());
            if (record.getUserId() == null) record.setUserId(userId);
            record.setPoints(record.getPoints() + points);
            sqlRepository.save(record);

            messagingTemplate.convertAndSend("/topic/leaderboard", getTopUsersRedis(NUMBER_TOP_USERS));
        } else {
            System.out.println("Failed to add points. Event " + eventId + " already exists");
        }

    }

    public List<LeaderboardEntryDto> getTopUsersRedis(int limit) {

        Set<ZSetOperations.TypedTuple<String>> topUsers = redisTemplate.opsForZSet()
                                                                       .reverseRangeWithScores(LEADERBOARD_KEY,
                                                                                               0,
                                                                                               limit - 1);
        if (topUsers == null || topUsers.isEmpty()) {
            return Collections.emptyList();
        }
        return topUsers.stream().map(tuple -> new LeaderboardEntryDto(tuple.getValue(), tuple.getScore())).collect(
                Collectors.toList());
    }


    public List<LeaderboardEntryDto> getTopUsersSql(int limit) {

        List<LeaderboardRecord> records = sqlRepository.findTopUsers(PageRequest.of(0, limit));
        return records.stream()
                      .map(r -> new LeaderboardEntryDto(r.getUserId(), r.getPoints()))
                      .collect(Collectors.toList());
    }


    public Long getUserRank(String userId) {

        Long rank = redisTemplate.opsForZSet().reverseRank(LEADERBOARD_KEY, userId);
        return rank != null ? rank + 1 : null;
    }
}

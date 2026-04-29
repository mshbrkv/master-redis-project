package org.example.leaderboardservice.service;


import lombok.RequiredArgsConstructor;
import org.example.leaderboardservice.model.LeaderboardRecord;
import org.example.leaderboardservice.repository.LeaderboardRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BenchmarkService {

    private static final String LEADERBOARD_KEY = "leaderboard";
    private final LeaderboardRepository sqlRepository;
    private final StringRedisTemplate redisTemplate;

    public void seedData(int count) {

        List<LeaderboardRecord> sqlRecords = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> redisRecords = new HashSet<>();

        for (int i = 0; i < count; i++) {
            String fakeUserId = UUID.randomUUID().toString();
            double randomPoints = Math.round(Math.random() * 10000);

            LeaderboardRecord record = new LeaderboardRecord();
            record.setUserId(fakeUserId);
            record.setPoints(randomPoints);
            sqlRecords.add(record);

            redisRecords.add(new DefaultTypedTuple<>(fakeUserId, randomPoints));
        }

        sqlRepository.saveAll(sqlRecords);

        redisTemplate.opsForZSet().add(LEADERBOARD_KEY, redisRecords);

    }

    public Map<String, Object> runBenchmark(int iterations) {

        Map<String, Object> result = new HashMap<>();
        long sqlTotalTime = 0;
        long redisTotalTime = 0;

        sqlRepository.findTopUsers(PageRequest.of(0, 10));
        redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, 0, 9);


        //PostgreSQL
        long startSql = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            sqlRepository.findTopUsers(PageRequest.of(0, 10));
        }
        sqlTotalTime = System.currentTimeMillis() - startSql;

        //Redis
        long startRedis = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, 0, 9);
        }
        redisTotalTime = System.currentTimeMillis() - startRedis;


        result.put("iterations", iterations);
        result.put("sql_total_time_ms", sqlTotalTime);
        result.put("redis_total_time_ms", redisTotalTime);
        result.put("sql_average_ms", (double) sqlTotalTime / iterations);
        result.put("redis_average_ms", (double) redisTotalTime / iterations);

        double multiplier = (double) sqlTotalTime / (redisTotalTime == 0 ? 1 : redisTotalTime);
        result.put("conclusion", "Redis is faster PostgreSQL in " + Math.round(multiplier) + " times.");
        return result;
    }
}

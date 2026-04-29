package org.example.leaderboardservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.leaderboardservice.dto.LeaderboardEntryDto;
import org.example.leaderboardservice.service.BenchmarkService;
import org.example.leaderboardservice.service.LeaderboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;
    private final BenchmarkService benchmarkService;

    @GetMapping("/redis/top")
    public ResponseEntity<List<LeaderboardEntryDto>> getTopUsersRedis(@RequestParam(defaultValue = "10") int limit) {

        List<LeaderboardEntryDto> topUsers = leaderboardService.getTopUsersRedis(limit);
        return ResponseEntity.ok(topUsers);
    }
    @GetMapping("/sql/top")
    public ResponseEntity<List<LeaderboardEntryDto>> getTopUsersSQL(@RequestParam(defaultValue = "10") int limit) {

        List<LeaderboardEntryDto> topUsers = leaderboardService.getTopUsersSql(limit);
        return ResponseEntity.ok(topUsers);
    }


    @GetMapping("/rank/{userId}")
    public ResponseEntity<String> getUserRank(@PathVariable String userId) {

        Long rank = leaderboardService.getUserRank(userId);
        if (rank == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok("Current place: " + rank);
    }

    @PostMapping("/test/seed")
    public ResponseEntity<Void> seedData(@RequestParam(defaultValue = "50000") int count) {

        benchmarkService.seedData(count);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test/run")
    public ResponseEntity<Map<String, Object>> runBenchmark(@RequestParam(defaultValue = "100") int iterations) {

        return ResponseEntity.ok(benchmarkService.runBenchmark(iterations));
    }



}

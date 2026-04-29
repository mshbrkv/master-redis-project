package org.example.leaderboardservice.repository;

import org.example.leaderboardservice.model.LeaderboardRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeaderboardRepository extends JpaRepository<LeaderboardRecord, String> {


    @Query("SELECT l FROM LeaderboardRecord l ORDER BY l.points DESC")
    List<LeaderboardRecord> findTopUsers(Pageable pageable);
}

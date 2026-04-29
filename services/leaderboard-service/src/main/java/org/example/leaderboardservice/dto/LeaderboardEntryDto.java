package org.example.leaderboardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardEntryDto {
private  String userId;
private double points;
}

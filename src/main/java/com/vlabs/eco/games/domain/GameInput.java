package com.vlabs.eco.games.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameInput {
    public String id;
    public String city;
    public String date;
    public String player_of_match;
    public String venue;
    public String neutral_venue;
    public String team1;
    public String team2;
    public String toss_winner;
    public String toss_decision;
    public String winner;
    public String result;
    public String result_margin;
    public String eliminator;
    public String method;
    public String umpire1;
    public String umpire2;
}

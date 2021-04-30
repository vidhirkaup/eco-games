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
    public String playerOfMatch;
    public String venue;
    public String neutralVenue;
    public String team1;
    public String team2;
    public String tossWinner;
    public String tossDecision;
    public String winner;
    public String result;
    public String resultMargin;
    public String eliminator;
    public String method;
    public String umpire1;
    public String umpire2;
}

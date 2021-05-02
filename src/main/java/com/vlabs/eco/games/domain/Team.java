package com.vlabs.eco.games.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String teamName;
    private long totalGames;
    private long totalWins;

    @Transient
    private List<Game> games;

    public Team(String teamName, long totalGames){
        this.teamName = teamName;
        this.totalGames = totalGames;
    }
}

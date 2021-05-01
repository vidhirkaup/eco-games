package com.vlabs.eco.games.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    public Team(String teamName, long totalGames){
        this.teamName = teamName;
        this.totalGames = totalGames;
    }
}

package com.vlabs.eco.games.controller;

import com.vlabs.eco.games.domain.Team;
import com.vlabs.eco.games.repository.GameRepository;
import com.vlabs.eco.games.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class TeamController {

    private TeamRepository teamRepository;

    private GameRepository gameRepository;

    public TeamController(TeamRepository teamRepository, GameRepository gameRepository) {
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
    }

    @GetMapping("/teams")
    public List<Team> getTeams(){
        return (List<Team>) teamRepository.findAll();
    }

    @GetMapping("/teams/{teamName}")
    public Team getTeam(@PathVariable String teamName){
        Team team = teamRepository.findByTeamName(teamName);

        team.setGames(gameRepository.getByTeam1OrTeam2OrderByDateDesc(teamName, teamName));

        return team;
    }

}

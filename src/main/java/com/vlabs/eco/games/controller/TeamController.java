package com.vlabs.eco.games.controller;

import com.vlabs.eco.games.domain.Game;
import com.vlabs.eco.games.domain.Team;
import com.vlabs.eco.games.service.GameService;
import com.vlabs.eco.games.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
public class TeamController {

    private final TeamService teamService;

    private final GameService gameService;

    public TeamController(TeamService teamService, GameService gameService) {
        this.teamService = teamService;
        this.gameService = gameService;
    }

    @GetMapping("/teams")
    public List<Team> getTeams() {
        log.info("getting all teams");
        return teamService.getAllTeams();
    }

    @GetMapping("/teams/{teamName}")
    public Team getTeam(@PathVariable String teamName) {
        log.info(String.format("get details for [%s]", teamName));
        return teamService.getTeam(teamName);
    }

    @GetMapping("/teams/{teamName}/plays")
    public List<Game> getGames(@PathVariable String teamName, @RequestParam int year) {
        log.info(String.format("get games for [%s] in [%s]", teamName, year));
        return gameService.getGames(teamName, year);
    }

}

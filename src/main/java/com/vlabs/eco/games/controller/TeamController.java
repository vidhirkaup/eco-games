package com.vlabs.eco.games.controller;

import com.vlabs.eco.games.domain.Team;
import com.vlabs.eco.games.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
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

}

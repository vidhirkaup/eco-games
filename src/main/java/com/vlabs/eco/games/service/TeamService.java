package com.vlabs.eco.games.service;

import com.vlabs.eco.games.domain.Team;
import com.vlabs.eco.games.repository.GameRepository;
import com.vlabs.eco.games.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    private final GameRepository gameRepository;

    public TeamService(TeamRepository teamRepository, GameRepository gameRepository) {
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
    }

    public List<Team> getAllTeams() {
        return (List<Team>) teamRepository.findAll();
    }

    public Team getTeam(String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        team.setGames(gameRepository.findLatestGamesByTeam(teamName, 3));
        return team;
    }
}

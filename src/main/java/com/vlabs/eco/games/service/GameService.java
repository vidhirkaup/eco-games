package com.vlabs.eco.games.service;

import com.vlabs.eco.games.domain.Game;
import com.vlabs.eco.games.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GameService {

    private GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getGames(String teamName, int year) {
        LocalDate dateFrom = LocalDate.of(year, 1, 1);
        LocalDate dateTo = LocalDate.of(year + 1, 1, 1);
        return gameRepository.findGamesByYear(teamName, dateFrom, dateTo);
    }
}

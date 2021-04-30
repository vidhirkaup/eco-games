package com.vlabs.eco.games.batch;

import com.vlabs.eco.games.domain.Game;
import com.vlabs.eco.games.domain.GameInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;

@Slf4j
public class GameDataProcessor implements ItemProcessor<GameInput, Game> {
    @Override
    public Game process(GameInput gameInput) throws Exception {
        Game game = new Game();

        game.setId(Long.parseLong(gameInput.getId()));

        game.setCity(gameInput.getCity());

        game.setDate(LocalDate.parse(gameInput.getDate()));

        game.setPlayerOfMatch(gameInput.getPlayerOfMatch());

        game.setVenue(gameInput.getVenue());

        game.setNeutralVenue(gameInput.getNeutralVenue());

        determineTeams(game);

        game.setTossWinner(gameInput.getTossWinner());

        game.setTossDecision(gameInput.getTossDecision());

        game.setWinner(gameInput.getWinner());

        game.setResult(gameInput.getResult());

        game.setResultMargin(gameInput.getResultMargin());

        game.setEliminator(gameInput.getEliminator());

        game.setMethod(gameInput.getMethod());

        game.setUmpire1(gameInput.getUmpire1());

        game.setUmpire2(gameInput.getUmpire2());

        log.info(game.toString());
        return game;
    }

    private void determineTeams(Game game) {
        String firstInningsTeam = game.getTeam1();
        String secondInningsTeam = game.getTeam2();
        String tossDecision = game.getTossDecision();
        String tossWinner = game.getTossWinner();

        if ("bat".equals(tossDecision)) {
            if (tossWinner.equals(firstInningsTeam)) {
                game.setTeam1(firstInningsTeam);
                game.setTeam2(secondInningsTeam);
            } else {
                game.setTeam1(secondInningsTeam);
                game.setTeam2(firstInningsTeam);
            }
        }

        if ("field".equals(tossDecision)) {
            if (tossWinner.equals(firstInningsTeam)) {
                game.setTeam1(secondInningsTeam);
                game.setTeam2(firstInningsTeam);
            } else {
                game.setTeam1(firstInningsTeam);
                game.setTeam2(secondInningsTeam);
            }
        }
    }
}

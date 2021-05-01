package com.vlabs.eco.games.batch;

import com.vlabs.eco.games.domain.Game;
import com.vlabs.eco.games.domain.GameInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;

@Slf4j
public class GameItemProcessor implements ItemProcessor<GameInput, Game> {
    @Override
    public Game process(GameInput gameInput) throws Exception {
        Game game = new Game();

        game.setId(Long.parseLong(gameInput.getId()));
        game.setCity(gameInput.getCity());
        game.setDate(LocalDate.parse(gameInput.getDate()));
        game.setPlayerOfMatch(gameInput.getPlayer_of_match());
        game.setVenue(gameInput.getVenue());
        game.setNeutralVenue(gameInput.getNeutral_venue());

        determineTeams(gameInput, game);

        game.setTossWinner(gameInput.getToss_winner());
        game.setTossDecision(gameInput.getToss_decision());
        game.setWinner(gameInput.getWinner());
        game.setResult(gameInput.getResult());
        game.setResultMargin(gameInput.getResult_margin());
        game.setEliminator(gameInput.getEliminator());
        game.setMethod(gameInput.getMethod());
        game.setUmpire1(gameInput.getUmpire1());
        game.setUmpire2(gameInput.getUmpire2());

        return game;
    }

    private void determineTeams(GameInput gameInput, Game game) {
        String firstInningsTeam = gameInput.getTeam1();
        String secondInningsTeam = gameInput.getTeam2();
        String tossDecision = gameInput.getToss_decision();
        String tossWinner = gameInput.getToss_winner();

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

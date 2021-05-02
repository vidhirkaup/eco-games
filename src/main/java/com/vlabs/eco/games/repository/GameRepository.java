package com.vlabs.eco.games.repository;

import com.vlabs.eco.games.domain.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GameRepository extends CrudRepository<Game, Long> {

    public List<Game> getByTeam1OrTeam2(String team1, String team2);

}

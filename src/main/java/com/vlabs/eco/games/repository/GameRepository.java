package com.vlabs.eco.games.repository;

import com.vlabs.eco.games.domain.Game;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface GameRepository extends CrudRepository<Game, Long> {

    List<Game> getByTeam1OrTeam2OrderByDateDesc(String team1, String team2, Pageable pageable);

    List<Game> getByTeam1AndDateBetweenOrTeam2AndDateBetweenOrderByDateDesc(String team1, LocalDate date1, LocalDate date2, String team2, LocalDate date3, LocalDate date4);

    default List<Game> findLatestGamesByTeam(String teamName, int count) {
        return getByTeam1OrTeam2OrderByDateDesc(teamName, teamName, PageRequest.of(0, count));
    }

}

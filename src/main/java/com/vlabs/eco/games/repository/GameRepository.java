package com.vlabs.eco.games.repository;

import com.vlabs.eco.games.domain.Game;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface GameRepository extends CrudRepository<Game, Long> {

    List<Game> getByTeam1OrTeam2OrderByDateDesc(String team1, String team2, Pageable pageable);

    List<Game> getByTeam1AndDateBetweenOrTeam2AndDateBetweenOrderByDateDesc(String team1, LocalDate date1, LocalDate date2,
                                                                            String team2, LocalDate date3, LocalDate date4);

    default List<Game> findLatestGamesByTeam(String teamName, int count) {
        return getByTeam1OrTeam2OrderByDateDesc(teamName, teamName, PageRequest.of(0, count));
    }

    @Query("select g from Game g where (g.team1 = :teamName or g.team2 = :teamName) and (g.date between :date1 and :date2)" )
    List<Game> findGamesByYear(@Param("teamName") String teamName,
                               @Param("date1") LocalDate date1,
                               @Param("date2") LocalDate date2);

}

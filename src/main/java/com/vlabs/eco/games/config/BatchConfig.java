package com.vlabs.eco.games.config;

import com.vlabs.eco.games.batch.GameItemProcessor;
import com.vlabs.eco.games.domain.Game;
import com.vlabs.eco.games.domain.GameInput;
import com.vlabs.eco.games.domain.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    public static final String[] FIELD_NAMES = {
            "id", "city", "date", "player_of_match", "venue", "neutral_venue", "team1", "team2", "toss_winner", "toss_decision", "winner", "result", "result_margin", "eliminator", "method", "umpire1", "umpire2"
    };

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public FlatFileItemReader<GameInput> reader() {
        return new FlatFileItemReaderBuilder<GameInput>()
                .name("gameItemReader")
                .resource(new ClassPathResource("IPL Matches 2008-2020.csv"))
                .delimited()
                .names(FIELD_NAMES)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<GameInput>() {{
                    setTargetType(GameInput.class);
                }})
                .build();
    }

    @Bean
    public GameItemProcessor processor() {
        return new GameItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Game> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Game>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO game (id, city, date, player_of_match, venue, neutral_venue, team1, team2, toss_winner, toss_decision, winner, result, result_margin, eliminator, method, umpire1, umpire2) " +
                        "       VALUES (:id, :city, :date, :playerOfMatch, :venue, :neutralVenue, :team1, :team2, :tossWinner, :tossDecision, :winner, :result, :resultMargin, :eliminator, :method, :umpire1, :umpire2)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importGamesJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importGamesJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Game> writer) {
        return stepBuilderFactory.get("step1")
                .<GameInput, Game>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}

@Slf4j
@Component
class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final EntityManager em;

    @Autowired
    public JobCompletionNotificationListener(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {

            Map<String, Team> teamData = new HashMap<>();

            em.createQuery("select g.team1, count(g.team1) from Game g group by g.team1", Object[].class)
                    .getResultList()
                    .stream()
                    .map(e -> new Team((String) e[0], (long) e[1]))
                    .forEach(team -> teamData.put(team.getTeamName(), team));

            em.createQuery("select g.team2, count(g.team2) from Game g group by g.team2", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        Team team = teamData.get((String) e[0]);
                        team.setTotalGames(team.getTotalGames() + (long) e[1]);
                    });

            em.createQuery("select g.winner, count(g.winner) from Game g group by g.winner", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        Team team = teamData.get((String) e[0]);
                        if (team != null) {
                            team.setTotalWins((Long) e[1]);
                        }
                    });

            log.info(teamData.toString());
            teamData.values()
                    .forEach(team -> {
                        em.persist(team);
                    });
        }
    }
}

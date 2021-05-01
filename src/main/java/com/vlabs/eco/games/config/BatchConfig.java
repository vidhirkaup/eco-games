package com.vlabs.eco.games.config;

import com.vlabs.eco.games.batch.GameItemProcessor;
import com.vlabs.eco.games.domain.Game;
import com.vlabs.eco.games.domain.GameInput;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

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

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(">>> job started");
        super.beforeJob(jobExecution);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
            log.info("job complete <<<");
            jdbcTemplate.query("SELECT team1, team1, date from game",
                    (rs, row) -> String.format("Team 1: {%s}, Team 2: {%s}, Date: {%s}",
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3)))
                    .forEach(str -> log.info(str));
        }
        super.afterJob(jobExecution);
    }
}

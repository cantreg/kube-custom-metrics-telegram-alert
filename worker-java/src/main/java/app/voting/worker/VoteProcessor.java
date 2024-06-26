package app.voting.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class VoteProcessor {

    public static final String REDIS_VOTES_KEY = "votes";
    public static final String METRIC_NAME_VOTES_UNPROCESSED = "votes_unprocessed";

    private final JdbcTemplate jdbcTemplate;
    private final VoteRepository voteRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final MeterRegistry meterRegistry;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS votes (id VARCHAR(255) NOT NULL UNIQUE, vote VARCHAR(255) NOT NULL)");
        Gauge
            .builder(METRIC_NAME_VOTES_UNPROCESSED, redisTemplate.opsForList(),
                    (ListOperations<String, String> o) -> o.size(REDIS_VOTES_KEY))
            .register(meterRegistry);
    }

    @Scheduled(fixedDelay = 500)
    public void process() {
        try {
            populateMetrics();
            String json = redisTemplate.opsForList().leftPop(REDIS_VOTES_KEY, 500, TimeUnit.MILLISECONDS);
            if (json != null) {
                Vote vote = objectMapper.readValue(json, Vote.class);
                saveVote(vote);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveVote(Vote vote) {
        System.out.println(String.format("Processing vote for %s by %s'", vote.getVote(), vote.getId()));
        voteRepository.insertVote(vote.getId(), vote.getVote());
    }

    private void populateMetrics() {
        long votes = redisTemplate.opsForList().size(REDIS_VOTES_KEY);

        System.out.println("Redis list size: " + votes);
    }
}
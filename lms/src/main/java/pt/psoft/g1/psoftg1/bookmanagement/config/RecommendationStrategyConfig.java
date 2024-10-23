package pt.psoft.g1.psoftg1.bookmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.bookmanagement.services.RecommendationStrategy;

@Configuration
public class RecommendationStrategyConfig {

    @Bean
    @Profile("age_based")
    public RecommendationStrategy ageBasedStrategy() {
        return RecommendationStrategy.AGE_BASED;
    }

    @Bean
    @Profile("genre_based")
    public RecommendationStrategy genreBasedStrategy() {
        return RecommendationStrategy.GENRE_BASED;
    }

}

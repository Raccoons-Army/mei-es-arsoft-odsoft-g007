package pt.psoft.g1.psoftg1.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.shared.strategies.HashIdGenerationStrategy;
import pt.psoft.g1.psoftg1.shared.strategies.HexIdGenerationStrategy;
import pt.psoft.g1.psoftg1.shared.model.IdGenerationStrategy;

@Configuration
public class IdGenerationConfig {

    @Bean
    @Profile("hash")
    public IdGenerationStrategy<String> hashIdGenerationStrategy() {
        return new HashIdGenerationStrategy();
    }

    @Bean
    @Profile("hex")
    public IdGenerationStrategy<String> hexIdGenerationStrategy() {
        return new HexIdGenerationStrategy();
    }
}

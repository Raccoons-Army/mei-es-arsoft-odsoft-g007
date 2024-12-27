package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.model.ReaderDetailsEvents;
import pt.psoft.g1.psoftg1.shared.model.SuggestionEvents;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionEventRabbitMQReceiver;
import pt.psoft.g1.psoftg1.suggestionmanagement.services.SuggestionService;

@Profile("!test")
@Configuration
public class RabbitmqClientConfig {


    public static final String DB_SYNC_QUEUE = "suggestions_db_sync_queue";

    @Bean
    public Queue suggestionDbSyncQueue() {
        return new Queue(DB_SYNC_QUEUE, false);
    }

    // Readers  Exchange
    @Bean(name = "readersExchange")
    public DirectExchange readersExchange() {
        return new DirectExchange("LMS.readers");
    }

    // Recommendation Exchange
    @Bean(name = "suggestionsExchange")
    public DirectExchange suggestionsExchange() {
        return new DirectExchange("LMS.suggestions");
    }

    private static class ReceiverConfig {

        // Readers  Queues
        @Bean(name = "autoDeleteQueue_Reader_Created")
        public Queue autoDeleteQueue_Reader_Created() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Reader_Deleted")
        public Queue autoDeleteQueue_Reader_Deleted() {
            return new AnonymousQueue();
        }

        // Suggestions Queues
        @Bean(name = "autoDeleteQueue_Suggestion_Created")
        public Queue autoDeleteQueue_Suggestion_Created() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Suggestion_Updated")
        public Queue autoDeleteQueue_Suggestion_Updated() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Suggestion_Deleted")
        public Queue autoDeleteQueue_Suggestion_Deleted() {
            return new AnonymousQueue();
        }

        // Readers  Bindings
        @Bean
        public Binding readerCreatedBinding(@Qualifier("readersExchange") DirectExchange readersExchange,
                                            @Qualifier("autoDeleteQueue_Reader_Created") Queue autoDeleteQueue_Reader_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Created)
                    .to(readersExchange)
                    .with(ReaderDetailsEvents.READER_CREATED);
        }

        @Bean
        public Binding readerDeletedBinding(@Qualifier("readersExchange") DirectExchange readersExchange,
                                            @Qualifier("autoDeleteQueue_Reader_Deleted") Queue autoDeleteQueue_Reader_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Deleted)
                    .to(readersExchange)
                    .with(ReaderDetailsEvents.READER_DELETED);
        }

        // Suggestions Bindings
        @Bean
        public Binding suggestionCreatedBinding(@Qualifier("suggestionsExchange") DirectExchange suggestionsExchange,
                                            @Qualifier("autoDeleteQueue_Suggestion_Created") Queue autoDeleteQueue_Suggestion_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Suggestion_Created)
                    .to(suggestionsExchange)
                    .with(SuggestionEvents.SUGGESTION_CREATED);
        }

        @Bean
        public Binding suggestionUpdatedBinding(@Qualifier("suggestionsExchange") DirectExchange suggestionsExchange,
                                            @Qualifier("autoDeleteQueue_Suggestion_Updated") Queue autoDeleteQueue_Suggestion_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Suggestion_Updated)
                    .to(suggestionsExchange)
                    .with(SuggestionEvents.SUGGESTION_UPDATED);
        }

        @Bean
        public Binding suggestionDeletedBinding(@Qualifier("suggestionsExchange") DirectExchange suggestionsExchange,
                                                @Qualifier("autoDeleteQueue_Suggestion_Deleted") Queue autoDeleteQueue_Suggestion_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Suggestion_Deleted)
                    .to(suggestionsExchange)
                    .with(SuggestionEvents.SUGGESTION_DELETED);
        }

        //  Receivers

        @Bean
        public ReaderDetailsEventRabbitmqReceiver readerReceiver(ReaderService readerService) {
            return new ReaderDetailsEventRabbitmqReceiver(readerService);
        }

        @Bean
        public SuggestionEventRabbitMQReceiver suggestionReceiver(SuggestionService suggestionService) {
            return new SuggestionEventRabbitMQReceiver(suggestionService);
        }

    }
}

package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.recommendationmanagement.services.RecommendationService;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;
import pt.psoft.g1.psoftg1.shared.model.ReaderEvents;
import pt.psoft.g1.psoftg1.shared.model.RecommendationEvents;

@Profile("!test")
@Configuration
public class RabbitmqClientConfig {

    @Bean(name = "booksExchange")
    public DirectExchange direct() {
        return new DirectExchange("LMS.books");
    }

    // Readers  Exchange
    @Bean(name = "readersExchange")
    public DirectExchange readersExchange() {
        return new DirectExchange("LMS.readers");
    }

    // Recommendation Exchange
    @Bean(name = "recommendationsExchange")
    public DirectExchange recommendationsExchange() {
        return new DirectExchange("LMS.recommendations");
    }

    private static class ReceiverConfig {

        // Books Queues
        @Bean(name = "autoDeleteQueue_Book_Created")
        public Queue autoDeleteQueue_Book_Created() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Book_Deleted")
        public Queue autoDeleteQueue_Book_Deleted() {
            return new AnonymousQueue();
        }

        // Readers  Queues
        @Bean(name = "autoDeleteQueue_Reader_Created")
        public Queue autoDeleteQueue_Reader_Created() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Reader_Deleted")
        public Queue autoDeleteQueue_Reader_Deleted() {
            return new AnonymousQueue();
        }

        // Recommendations Queues
        @Bean(name = "autoDeleteQueue_Recommendation_Created")
        public Queue autoDeleteQueue_Recommendation_Created() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Recommendation_Updated")
        public Queue autoDeleteQueue_Recommendation_Updated() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Recommendation_Rpc")
        public Queue autoDeleteQueue_Recommendation_Rpc() {
            return new AnonymousQueue();
        }


        // Books Bindings
        @Bean
        public Binding bookCreatedBinding(@Qualifier("booksExchange") DirectExchange booksExchange,
                                          @Qualifier("autoDeleteQueue_Book_Created") Queue autoDeleteQueue_Book_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Created)
                    .to(booksExchange)
                    .with(BookEvents.BOOK_CREATED);
        }

        @Bean
        public Binding bookDeletedBinding(@Qualifier("booksExchange") DirectExchange booksExchange,
                                          @Qualifier("autoDeleteQueue_Book_Deleted") Queue autoDeleteQueue_Book_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Deleted)
                    .to(booksExchange)
                    .with(BookEvents.BOOK_DELETED);
        }

        // Readers  Bindings
        @Bean
        public Binding readerCreatedBinding(@Qualifier("readersExchange") DirectExchange readersExchange,
                                            @Qualifier("autoDeleteQueue_Reader_Created") Queue autoDeleteQueue_Reader_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Created)
                    .to(readersExchange)
                    .with(ReaderEvents.READER_CREATED);
        }

        @Bean
        public Binding readerDeletedBinding(@Qualifier("readersExchange") DirectExchange readersExchange,
                                            @Qualifier("autoDeleteQueue_Reader_Deleted") Queue autoDeleteQueue_Reader_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Deleted)
                    .to(readersExchange)
                    .with(ReaderEvents.READER_DELETED);
        }

        // Recommendations Bindings
        @Bean
        public Binding recommendationCreatedBinding(@Qualifier("recommendationsExchange") DirectExchange recommendationsExchange,
                                            @Qualifier("autoDeleteQueue_Recommendation_Created") Queue autoDeleteQueue_Recommendation_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Recommendation_Created)
                    .to(recommendationsExchange)
                    .with(RecommendationEvents.RECOMMENDATION_CREATED);
        }

        @Bean
        public Binding recommendationUpdatedBinding(@Qualifier("recommendationsExchange") DirectExchange recommendationsExchange,
                                            @Qualifier("autoDeleteQueue_Recommendation_Updated") Queue autoDeleteQueue_Recommendation_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Recommendation_Updated)
                    .to(recommendationsExchange)
                    .with(RecommendationEvents.RECOMMENDATION_UPDATED);
        }

        @Bean
        public Binding recommendationRpcBinding(@Qualifier("recommendationsExchange") DirectExchange recommendationsExchange,
                                            @Qualifier("autoDeleteQueue_Recommendation_Rpc") Queue autoDeleteQueue_Recommendation_Rpc) {
            return BindingBuilder.bind(autoDeleteQueue_Recommendation_Rpc)
                    .to(recommendationsExchange)
                    .with(RecommendationEvents.RECOMMENDATION_RPC);
        }

        //  Receivers
        @Bean
        public BookEventRabbitmqReceiver receiver(BookService bookService) {
            return new BookEventRabbitmqReceiver(bookService);
        }

        @Bean
        public ReaderEventRabbitmqReceiver readerReceiver(ReaderService readerService) {
            return new ReaderEventRabbitmqReceiver(readerService);
        }

        @Bean
        public RecommendationEventRabbitmqReceiver recommendationReceiver(RecommendationService recommendationService) {
            return new RecommendationEventRabbitmqReceiver(recommendationService);
        }

    }
}

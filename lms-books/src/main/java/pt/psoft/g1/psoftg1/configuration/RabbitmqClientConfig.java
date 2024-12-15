package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.shared.model.AuthorEvents;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;
import pt.psoft.g1.psoftg1.shared.model.GenreEvents;
import pt.psoft.g1.psoftg1.shared.model.SuggestionEvents;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.api.SuggestionEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.service.SuggestedBookService;

@Profile("!test")
@Configuration
public class RabbitmqClientConfig {

    @Bean(name = "booksExchange")
    public DirectExchange direct() {
        return new DirectExchange("LMS.books");
    }

    @Bean(name = "authorsExchange")
    public DirectExchange authorsExchange() {
        return new DirectExchange("LMS.authors");
    }

    @Bean(name = "genresExchange")
    public DirectExchange genresExchange() {
        return new DirectExchange("LMS.genres");
    }

    @Bean(name = "suggestionsExchange")
    public DirectExchange suggestionsExchange() {
        return new DirectExchange("LMS.suggestions");
    }

    private static class ReceiverConfig {

        // Books Queues
        @Bean(name = "autoDeleteQueue_Book_Created")
        public Queue autoDeleteQueue_Book_Created() {
            System.out.println("autoDeleteQueue_Book_Created created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Book_Updated")
        public Queue autoDeleteQueue_Book_Updated() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Book_Deleted")
        public Queue autoDeleteQueue_Book_Deleted() {
            return new AnonymousQueue();
        }

        // Authors Queues
        @Bean(name = "autoDeleteQueue_Author_Created")
        public Queue autoDeleteQueue_Author_Created() {
            System.out.println("autoDeleteQueue_Author_Created created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Author_Updated")
        public Queue autoDeleteQueue_Author_Updated() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Author_Deleted")
        public Queue autoDeleteQueue_Author_Deleted() {
            return new AnonymousQueue();
        }

        // Genres Queues
        @Bean(name = "autoDeleteQueue_Genre_Created")
        public Queue autoDeleteQueue_Genre_Created() {
            System.out.println("autoDeleteQueue_Genre_Created created!");
            return new AnonymousQueue();
        }

        // Suggestions Queues
        @Bean(name = "autoDeleteQueue_Suggestion_Created")
        public Queue autoDeleteQueue_Suggestion_Created() {
            System.out.println("autoDeleteQueue_Suggestion_Created created!");
            return new AnonymousQueue();
        }

        //  Book bindings
        @Bean
        public Binding bookCreatedBinding(@Qualifier("booksExchange") DirectExchange direct,
                                          @Qualifier("autoDeleteQueue_Book_Created") Queue autoDeleteQueue_Book_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Created)
                    .to(direct)
                    .with(BookEvents.BOOK_CREATED);
        }

        @Bean
        public Binding bookUpdatedBinding(@Qualifier("booksExchange") DirectExchange direct,
                                          @Qualifier("autoDeleteQueue_Book_Updated") Queue autoDeleteQueue_Book_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Updated)
                    .to(direct)
                    .with(BookEvents.BOOK_UPDATED);
        }

        @Bean
        public Binding bookDeleteBinding(@Qualifier("booksExchange") DirectExchange direct,
                                         @Qualifier("autoDeleteQueue_Book_Deleted") Queue autoDeleteQueue_Book_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Deleted)
                    .to(direct)
                    .with(BookEvents.BOOK_DELETED);
        }

        // Author bindings
        @Bean
        public Binding authorCreatedBinding(@Qualifier("authorsExchange") DirectExchange direct,
                                            @Qualifier("autoDeleteQueue_Author_Created") Queue autoDeleteQueue_Author_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Author_Created)
                    .to(direct)
                    .with(AuthorEvents.AUTHOR_CREATED);
        }

        @Bean
        public Binding authorUpdatedBinding(@Qualifier("authorsExchange") DirectExchange direct,
                                            @Qualifier("autoDeleteQueue_Author_Updated") Queue autoDeleteQueue_Author_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Author_Updated)
                    .to(direct)
                    .with(AuthorEvents.AUTHOR_UPDATED);
        }

        @Bean
        public Binding authorDeleteBinding(@Qualifier("authorsExchange") DirectExchange direct,
                                           @Qualifier("autoDeleteQueue_Author_Deleted") Queue autoDeleteQueue_Author_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Author_Deleted)
                    .to(direct)
                    .with(AuthorEvents.AUTHOR_DELETED);
        }

        // Genre bindings
        @Bean
        public Binding genreCreatedBinding(@Qualifier("genresExchange") DirectExchange direct,
                                           @Qualifier("autoDeleteQueue_Genre_Created") Queue autoDeleteQueue_Genre_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Genre_Created)
                    .to(direct)
                    .with(GenreEvents.GENRE_CREATED);
        }

        //  Suggestion bindings
        @Bean
        public Binding suggestionCreatedBinding(@Qualifier("suggestionsExchange") DirectExchange direct,
                                           @Qualifier("autoDeleteQueue_Suggestion_Created") Queue autoDeleteQueue_Suggestion_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Suggestion_Created)
                    .to(direct)
                    .with(SuggestionEvents.SUGGESTION_CREATED);
        }



        //  Receivers
        @Bean
        public BookEventRabbitmqReceiver receiver(BookService bookService) {
            return new BookEventRabbitmqReceiver(bookService);
        }

        @Bean
        public AuthorEventRabbitmqReceiver authorReceiver(AuthorService authorService) {
            return new AuthorEventRabbitmqReceiver(authorService);
        }

        @Bean
        public GenreEventRabbitmqReceiver genreReceiver(GenreService genreService) {
            return new GenreEventRabbitmqReceiver(genreService);
        }

        @Bean
        public SuggestionEventRabbitmqReceiver receiver(SuggestedBookService suggestedBookService) {
            return new SuggestionEventRabbitmqReceiver(suggestedBookService);
        }
    }
}

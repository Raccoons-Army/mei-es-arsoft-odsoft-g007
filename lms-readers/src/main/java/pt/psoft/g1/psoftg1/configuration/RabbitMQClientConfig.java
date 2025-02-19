package pt.psoft.g1.psoftg1.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderEventRabbitMQReceiver;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.model.ReaderEvents;
import pt.psoft.g1.psoftg1.shared.model.UserEvents;
import pt.psoft.g1.psoftg1.usermanagement.api.UserEventRabbitMQReceiver;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

@Profile("!test")
@Configuration
public class RabbitMQClientConfig {


    public static final String DB_SYNC_QUEUE = "readers_db_sync_queue";
    public static final String DB_USERS_SYNC_QUEUE = "auth_db_sync_queue";

    @Bean
    public Queue readersDbSyncQueue() {
        return new Queue(DB_SYNC_QUEUE, false);
    }

    @Bean
    public Queue usersDbSyncQueue() {
        return new Queue(DB_USERS_SYNC_QUEUE, false);
    }

    @Bean(name = "readersExchange")
    public DirectExchange direct() {
        return new DirectExchange("LMS.readers");
    }

    @Bean(name = "usersExchange")
    public DirectExchange directUsers() {
        return new DirectExchange("LMS.users");
    }

    private static class ReceiverConfig {

        @Bean(name = "autoDeleteQueue_Reader_Created")
        public Queue autoDeleteQueue_Reader_Created() {
            System.out.println("autoDeleteQueue_Reader_Created created!");
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Reader_Updated() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Reader_Deleted() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_User_Created")
        public Queue autoDeleteQueue_User_Created() {

            System.out.println("autoDeleteQueue_User_Created created!");
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1(@Qualifier("readersExchange") DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Reader_Created") Queue autoDeleteQueue_Reader_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Created)
                    .to(direct)
                    .with(ReaderEvents.READER_CREATED);
        }

        @Bean
        public Binding binding2(@Qualifier("readersExchange") DirectExchange direct,
                                Queue autoDeleteQueue_Reader_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Updated)
                    .to(direct)
                    .with(ReaderEvents.READER_UPDATED);
        }

        @Bean
        public Binding binding3(@Qualifier("readersExchange") DirectExchange direct,
                                Queue autoDeleteQueue_Reader_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Deleted)
                    .to(direct)
                    .with(ReaderEvents.READER_DELETED);
        }

        @Bean
        public Binding binding4(@Qualifier("usersExchange") DirectExchange direct,
                                Queue autoDeleteQueue_User_Created) {
            return BindingBuilder.bind(autoDeleteQueue_User_Created)
                    .to(direct)
                    .with(UserEvents.USER_CREATED);
        }

        @Bean
        public ReaderEventRabbitMQReceiver receiver(ReaderService readerService) {
            return new ReaderEventRabbitMQReceiver(readerService);
        }

        @Bean
        public UserEventRabbitMQReceiver userReceiver(UserService userService) {
            return new UserEventRabbitMQReceiver(userService);
        }
    }
}

package pt.psoft.g1.psoftg1.publishers;

import pt.psoft.g1.psoftg1.usermanagement.model.User;

public interface UserEventsPublisher {

    void sendUserCreated(User user);

    void sendUserUpdated(User user, Long currentVersion);

    void sendUserDeleted(User user, Long currentVersion);
}

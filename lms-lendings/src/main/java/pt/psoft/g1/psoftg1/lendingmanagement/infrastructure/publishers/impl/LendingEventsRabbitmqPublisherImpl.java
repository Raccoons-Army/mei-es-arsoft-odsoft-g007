package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.publishers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQPMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.publishers.LendingEventPublisher;
import pt.psoft.g1.psoftg1.shared.model.LendingEvents;

@Service
@RequiredArgsConstructor
public class LendingEventsRabbitmqPublisherImpl implements LendingEventPublisher {
    @Autowired
    private RabbitTemplate template;
    @Qualifier("lendingsExchange")
    private DirectExchange direct;
    private final LendingViewAMQPMapper lendingViewAMQPMapper;

    @Override
    public void sendLendingCreated(Lending lending) {
        sendLendingEvent(lending, lending.getVersion(), LendingEvents.LENDING_CREATED);
    }

    @Override
    public void sendLendingUpdated(Lending lending, Long currentVersion) {
        sendLendingEvent(lending, currentVersion, LendingEvents.LENDING_UPDATED);
    }

    @Override
    public void sendLendingDeleted(Lending lending, Long currentVersion) {
        sendLendingEvent(lending, currentVersion, LendingEvents.LENDING_DELETED);
    }

    private void sendLendingEvent(Lending lending, long version, String lendingEventType) {
        try {
            LendingViewAMQP lendingViewAMQP = lendingViewAMQPMapper.toLendingViewAMQP(lending);
            lendingViewAMQP.setVersion(version);

            this.template.convertAndSend(direct.getName(), lendingEventType, lendingViewAMQP);

            System.out.println(" [x] Sent '" + lendingViewAMQP + "'");
        } catch (Exception ex) {
            System.out.println(" [x] Exception sending lending event: '" + ex.getMessage() + "'");
        }
    }
}

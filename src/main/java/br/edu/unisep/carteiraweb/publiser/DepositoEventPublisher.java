package br.edu.unisep.carteiraweb.publiser;

import br.edu.unisep.carteiraweb.event.DepositoEvent;
import br.edu.unisep.carteiraweb.model.Transacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DepositoEventPublisher {

    @Autowired
    private final ApplicationEventPublisher eventPublisher;

    public DepositoEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishEvent(Transacao transacao) {
        DepositoEvent depositoEvent = new DepositoEvent(transacao, transacao);
        eventPublisher.publishEvent(depositoEvent);
    }
}

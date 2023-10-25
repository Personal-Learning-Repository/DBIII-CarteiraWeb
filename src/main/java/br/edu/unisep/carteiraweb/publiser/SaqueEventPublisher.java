package br.edu.unisep.carteiraweb.publiser;

import br.edu.unisep.carteiraweb.event.SaqueEvent;
import br.edu.unisep.carteiraweb.model.Transacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SaqueEventPublisher {

    @Autowired
    private final ApplicationEventPublisher eventPublisher;

    public SaqueEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishEvent(Transacao transacao) {
        SaqueEvent saqueEvent = new SaqueEvent(transacao, transacao);
        eventPublisher.publishEvent(saqueEvent);
    }

}

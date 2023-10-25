package br.edu.unisep.carteiraweb.event;

import br.edu.unisep.carteiraweb.model.Transacao;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SaqueEvent extends ApplicationEvent {


    private final Transacao transacao;

    public SaqueEvent(Object source, Transacao transacao) {
        super(source);
        this.transacao = transacao;
    }

}

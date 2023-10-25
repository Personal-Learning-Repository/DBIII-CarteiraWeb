package br.edu.unisep.carteiraweb.controller;

import br.edu.unisep.carteiraweb.exception.ResourceNotFoundException;
import br.edu.unisep.carteiraweb.helpers.GetUserByToken;
import br.edu.unisep.carteiraweb.model.Transacao;
import br.edu.unisep.carteiraweb.model.Usuario;
import br.edu.unisep.carteiraweb.publiser.DepositoEventPublisher;
import br.edu.unisep.carteiraweb.publiser.SaqueEventPublisher;
import br.edu.unisep.carteiraweb.repository.TransacaoRepository;
import br.edu.unisep.carteiraweb.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TransacaoController {

    //TODO Atualizado em
    //TODO pegar usuario da sessão

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DepositoEventPublisher depositoEventPublisher;

    @Autowired
    private SaqueEventPublisher saqueEventPublisher;

    @Autowired
    private GetUserByToken getUserByToken;

    //TODO Get by data / intervalo
    //TODO Apenas transações do usuário
    //TODO Código repetido
    //TODO DTOs ?
    //TODO Tratamento de erro autenticação
    @GetMapping("/transacoes")
    public List<Transacao> getAllTransacoes()
            throws ResourceNotFoundException {

        Usuario usuario = getUserByToken.execute();

        return transacaoRepository.findByUsuario(usuario.getId());
    }

    @PostMapping("/transacoes/deposito")
    public Transacao deposito(@RequestBody Transacao transacao)
            throws ResourceNotFoundException {

        Usuario usuario = getUserByToken.execute();

        transacao.setUsuario(usuario);
        //TODO Date Util
        transacao.setData(new Date());

        if (transacao.getDescricao() == null) {
            transacao.setDescricao("Depósito de R$" + transacao.getValor() + " para " + usuario.getNome());
        }

        transacao.setUsuario(usuario);

        Transacao newTransacao = transacaoRepository.save(transacao);

        //TODO Evento não vai se transação falhar
        depositoEventPublisher.publishEvent(newTransacao);

        return newTransacao;
    }

    @PostMapping("/transacoes/saque")
    public Transacao saque(@RequestBody Transacao transacao)
            throws ResourceNotFoundException {

        Usuario usuario = getUserByToken.execute();

        transacao.setUsuario(usuario);
        //TODO Date Util
        transacao.setData(new Date());

        //TODO Name email check
        if (transacao.getDescricao() == null) {
            transacao.setDescricao("Saque de R$" + transacao.getValor() + " para " + usuario.getNome());
        }

        transacao.setUsuario(usuario);

        if (usuario.getCarteira().getSaldo() - transacao.getValor() < 0) {
            throw new ResourceNotFoundException("Saldo insuficiente");
        }

        Transacao newTransacao = transacaoRepository.save(transacao);

        //TODO Evento não vai se transação falhar
        saqueEventPublisher.publishEvent(newTransacao);

        return newTransacao;
    }

    @PostMapping("/transacoes/transferencia")
    public Transacao transferencia(@RequestBody Transacao transacao)
            throws ResourceNotFoundException {

        Usuario remetente = getUserByToken.execute();

        Usuario destinatario = usuarioRepository.findById(transacao.getUsuario().getId()).orElseThrow(() ->
                new ResourceNotFoundException("Usuário não encontrado :: " + transacao.getUsuario().getId()));

        //TODO Date Util
        transacao.setData(new Date());

        if (transacao.getDescricao() == null) {
            transacao.setDescricao("Transferência de R$" +
                    transacao.getValor() + " para " +
                    destinatario.getNome() + " de " +
                    remetente.getNome());
        }

        transacao.setUsuario(destinatario);

        Transacao newTransacao = transacaoRepository.save(transacao);
        depositoEventPublisher.publishEvent(newTransacao);

        Transacao desTransacao = new Transacao();

        desTransacao.setValor(transacao.getValor());
        desTransacao.setUsuario(remetente);
        //TODO Date Util
        desTransacao.setData(new Date());
        desTransacao.setDescricao(transacao.getDescricao());

        //TODO Evento não vai se transação falhar
        saqueEventPublisher.publishEvent(desTransacao);

        return newTransacao;
    }

}

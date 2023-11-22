package br.edu.unisep.carteiraweb.controller;

import br.edu.unisep.carteiraweb.dto.UsuarioResponse;
import br.edu.unisep.carteiraweb.exception.ResourceNotFoundException;
import br.edu.unisep.carteiraweb.helpers.GetUserByToken;
import br.edu.unisep.carteiraweb.model.Carteira;
import br.edu.unisep.carteiraweb.model.Usuario;
import br.edu.unisep.carteiraweb.repository.CarteiraRepository;
import br.edu.unisep.carteiraweb.repository.UsuarioRepository;
import br.edu.unisep.carteiraweb.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UsuarioController {

    //TODO Pedir ajuda auths

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarteiraRepository carteiraRepository;

    @Autowired
    private GetUserByToken getUserByToken;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public List<UsuarioResponse> getAllUsers() {
        return usuarioService.build(usuarioRepository.findAll());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> getUserById(@PathVariable(value = "id") Long usuarioId)
            throws ResourceNotFoundException {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() ->
                new ResourceNotFoundException("User not found for this id :: " + usuarioId));
        UsuarioResponse displayDTO = usuarioService.build(usuario);
        return ResponseEntity.ok().body(displayDTO);
    }

    @GetMapping("/usuarios/saldo/{id}")
    public ResponseEntity<String> getSaldo(@PathVariable(value = "id") Long usuarioId)
            throws ResourceNotFoundException {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() ->
                new ResourceNotFoundException("User not found for this id :: " + usuarioId));

        String saldo = "Seu saldo é: R$" + usuario.getCarteira().getSaldo();
        return ResponseEntity.ok().body(saldo);
    }

    @GetMapping("/usuarios/test")

    @PostMapping("/usuarios")
    @Transactional
    public Usuario createUser(@Validated @RequestBody Usuario usuario)
            throws ResourceNotFoundException {

        Usuario currentUsuario = getUserByToken.execute();

        //TODO Date Util
        usuario.setCriadoEm(new Date());

        if (usuario.getNome() != null) {
            usuario.setCriadoPor(currentUsuario.getNome());
        } else {
            usuario.setCriadoPor(currentUsuario.getEmail());
        }

        Carteira carteira = new Carteira();

        usuario.setCarteira(carteira);

        carteiraRepository.save(carteira);

        return usuarioRepository.save(usuario);
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> updateUser(@PathVariable(value = "id") Long usuarioId,
                                              @Validated @RequestBody Usuario detalhes)
            throws ResourceNotFoundException {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + usuarioId));

        Usuario currentUsuario = getUserByToken.execute();

        if (detalhes.getCpf() != null) {
            usuario.setCpf(detalhes.getCpf());
        }

        if (detalhes.getNome() != null) {
            usuario.setNome(detalhes.getNome());
        }

        if (detalhes.getEmail() != null) {
            usuario.setEmail(detalhes.getEmail());
        }

        if (detalhes.getSenha() != null) {
            usuario.setSenha(detalhes.getSenha());
        }

        if (currentUsuario.getNome() != null) {
            usuario.setAtualizadoPor(currentUsuario.getNome());
        } else {
            usuario.setAtualizadoPor(currentUsuario.getEmail());
        }

        //TODO Data Util
        usuario.setAtualizadoEm(new Date());

        final Usuario updatedUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/usuarios/{id}")
    public Map<String, Boolean> deleteUsuario(
            @PathVariable(value = "id") Long usuarioId
    ) throws Exception {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado ::" + usuarioId));
        usuarioRepository.delete(usuario);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}

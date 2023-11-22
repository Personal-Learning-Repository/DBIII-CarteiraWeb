package br.edu.unisep.carteiraweb.service;


import br.edu.unisep.carteiraweb.dto.UsuarioResponse;
import br.edu.unisep.carteiraweb.model.Usuario;
import br.edu.unisep.carteiraweb.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioResponse build(Usuario usuario) {
        UsuarioResponse displayDTO = new UsuarioResponse();
        displayDTO.setCpf(usuario.getCpf());
        displayDTO.setNome(usuario.getNome());
        displayDTO.setEmail(usuario.getEmail());
        displayDTO.setSaldo(usuario.getCarteira().getSaldo());
        displayDTO.setCriadoEm(usuario.getCriadoEm());
        displayDTO.setCriadoPor(usuario.getCriadoPor());
        displayDTO.setAtualizadoEm(usuario.getAtualizadoEm());
        displayDTO.setAtualizadoPor(usuario.getAtualizadoPor());

        return displayDTO;
    }

    public List<UsuarioResponse> build(List<Usuario> usuarios) {
        return usuarios.stream().map(this::build).toList();
    }

}

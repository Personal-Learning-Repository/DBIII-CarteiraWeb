package br.edu.unisep.carteiraweb.builder;

import br.edu.unisep.carteiraweb.dto.DisplayUsuarioDTO;
import br.edu.unisep.carteiraweb.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioDTOBuilder {

    public DisplayUsuarioDTO build(Usuario usuario) {
        DisplayUsuarioDTO displayDTO = new DisplayUsuarioDTO();
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

    public List<DisplayUsuarioDTO> build(List<Usuario> usuarios) {
        return usuarios.stream().map(this::build).toList();
    }

}

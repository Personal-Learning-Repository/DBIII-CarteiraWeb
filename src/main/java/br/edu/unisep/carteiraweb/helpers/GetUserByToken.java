package br.edu.unisep.carteiraweb.helpers;

import br.edu.unisep.carteiraweb.exception.ResourceNotFoundException;
import br.edu.unisep.carteiraweb.model.Usuario;
import br.edu.unisep.carteiraweb.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class GetUserByToken {

    //TODO Conferir boa pratica
    //TODO Melhorar nome

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario execute() throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername());

        if (usuario == null) {
            throw new ResourceNotFoundException("Usuário não autenticado");
        }

        return usuario;
    }

}

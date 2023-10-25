package br.edu.unisep.carteiraweb.service;

import br.edu.unisep.carteiraweb.model.Carteira;
import br.edu.unisep.carteiraweb.model.Usuario;
import br.edu.unisep.carteiraweb.repository.CarteiraRepository;
import br.edu.unisep.carteiraweb.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarteiraRepository carteiraRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            throw new UsernameNotFoundException("Email não encontrado");
        }

        return new User(usuario.getEmail(), usuario.getSenha(), new ArrayList<>());
    }

    //TODO Mudar pra Controller ?
    @Transactional
    public Usuario save(Usuario usuario) {
        Usuario novo = new Usuario();
        novo.setCpf(usuario.getCpf());
        novo.setNome(usuario.getNome());
        novo.setEmail(usuario.getEmail());
        novo.setSenha(bcryptEncoder.encode(usuario.getSenha()));
        //TODO Date Util
        novo.setCriadoEm(new Date());

        if (usuario.getNome() != null) {
            novo.setCriadoPor(usuario.getNome());
        } else {
            novo.setCriadoPor(usuario.getEmail());
        }

        Carteira carteira = new Carteira();

        novo.setCarteira(carteira);

        carteiraRepository.save(carteira);

        return usuarioRepository.save(novo);
    }
}

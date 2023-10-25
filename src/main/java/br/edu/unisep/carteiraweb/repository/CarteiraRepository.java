package br.edu.unisep.carteiraweb.repository;

import br.edu.unisep.carteiraweb.model.Carteira;
import org.springframework.data.jpa.repository.JpaRepository;

// TODO @Repository
public interface CarteiraRepository extends JpaRepository<Carteira, Long> {
}

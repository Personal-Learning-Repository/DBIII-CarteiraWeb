package br.edu.unisep.carteiraweb.repository;

import br.edu.unisep.carteiraweb.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// TODO @Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {


    @Query(value = "select * from transacao where cod_usuario = :usuario", nativeQuery = true)
    List<Transacao> findByUsuario(Long usuario);

    @Query(value = "select * from Transacao where cod_usuario = :usuario " +
            "and data between :dataIni and :dataFim", nativeQuery = true)
    List<Transacao> findByDataInterval(Long usuario, String dataIni, String dataFim);
}

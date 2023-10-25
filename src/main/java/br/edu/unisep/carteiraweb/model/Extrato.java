package br.edu.unisep.carteiraweb.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "extrato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Extrato {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cod_usuario", nullable = false)
    private Usuario usuario;

    @JoinColumn(name = "data_inicio", nullable = false)
    private Date data_inicio;

    @JoinColumn(name = "data_fim", nullable = false)
    private Date data_fim;

    @Column(name = "downloads", nullable = false)
    @ColumnDefault(value = "2")
    private int downloads;

    @ManyToMany
    @JoinTable(
            name = "extrato_transacao",
            joinColumns = @JoinColumn(name = "cod_extrato", columnDefinition = "bigint DEFAULT 0"),
            inverseJoinColumns = @JoinColumn(name = "cod_transacao", columnDefinition = "bigint DEFAULT 0")
    )
    private List<Transacao> transacoes;

    public void decrementDownloads() {
        if (downloads > 0) {
            downloads--;
        }
    }

    //TODO Data do extrato ?
}

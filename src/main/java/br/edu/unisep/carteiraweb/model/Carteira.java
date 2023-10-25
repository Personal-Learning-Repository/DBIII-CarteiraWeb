package br.edu.unisep.carteiraweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "carteira")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Carteira {

    //TODO Conferir boa pratica

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "saldo", nullable = false)
    @ColumnDefault(value = "0.0")
    @org.hibernate.annotations.Generated
    private Double saldo;

    @JsonIgnore
    @OneToOne(mappedBy = "carteira")
    private Usuario usuario;

    @Column(name = "atualizado_em")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date atualizadoEm;

    @Column(name = "atualizado_por")
    @LastModifiedBy
    private String atualizadoPor;

}

package br.edu.unisep.carteiraweb.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UsuarioResponse {

    private String cpf;
    private String nome;
    private String email;
    private Double saldo;
    private Date criadoEm;
    private String criadoPor;
    private Date atualizadoEm;
    private String atualizadoPor;

    //TODO Mapper
    //TODO Boa pratica
}

package com.tcc.gelato.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

/**
 * O Usuário encompassa as contas de todos que usam a aplicação
 */
@Entity
@Table(name="usuario")
public class M_Usuario {

    public enum Cargo {
        CLIENTE,
        ENTREGADOR,
        GESTOR,
        ADMINISTRADOR
    }

    @Id
    @Column(nullable = false, unique = true)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private Cargo cargo;
}

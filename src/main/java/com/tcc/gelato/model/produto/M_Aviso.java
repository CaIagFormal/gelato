package com.tcc.gelato.model.produto;

import jakarta.persistence.*;

/**
 * Representa avisos médicos ou nutricionais
 * Ex.: Contém álcool, 0% álcool
 */
@Entity
@Table(name="aviso")
public class M_Aviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

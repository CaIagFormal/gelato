package com.tcc.gelato.model.produto;

import jakarta.persistence.*;

/**
 * Representa a classificação de um {@link M_Produto} e
 * pode especificar também um {@link M_Setor}
 */
@Entity
@Table(name="setor")
public class M_Setor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @JoinColumn(name="id_setor_parente")
    @ManyToOne
    private M_Setor setor_parente;

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

    public M_Setor getSetorParente() {
        return setor_parente;
    }

    public void setSetorParente(M_Setor setor_parente) {
        this.setor_parente = setor_parente;
    }
}

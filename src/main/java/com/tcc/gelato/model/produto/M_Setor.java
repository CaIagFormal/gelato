package com.tcc.gelato.model.produto;

import jakarta.persistence.*;

import java.util.List;

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

    @JoinColumn(name="id_setor")
    @OneToMany
    private List<M_Produto> produtos;

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

    public List<M_Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<M_Produto> produtos) {
        this.produtos = produtos;
    }
}

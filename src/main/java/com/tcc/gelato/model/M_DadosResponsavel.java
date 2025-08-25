package com.tcc.gelato.model;

import jakarta.persistence.*;

/**
 * Este model representa os dados de um reponsável de um usuário
 */
@Entity
@Table(name = "dados_responsavel")
public class M_DadosResponsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn( name = "fk_usuario",nullable = false)
    @ManyToOne
    private M_Usuario usuario;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String filiacao;

    @Column(nullable = false)
    private String contato;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public M_Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(M_Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFiliacao() {
        return filiacao;
    }

    public void setFiliacao(String filiacao) {
        this.filiacao = filiacao;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }
}

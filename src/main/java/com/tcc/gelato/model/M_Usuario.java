package com.tcc.gelato.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.Type;

import java.sql.Date;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(nullable = false)
    private Date data_nasc;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Date getDataNasc() {
        return data_nasc;
    }

    public void setDataNasc(Date data_nasc) {
        this.data_nasc = data_nasc;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
}

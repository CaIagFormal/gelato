package com.tcc.gelato.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Representa um acesso à uma funcionalidade enviado ao e-mail do usuário
 */
@Entity
@Table(name = "acesso_via_url")
public class M_AcessoViaUrl {

    public enum Funcionalidade {
        VERIFICAR_CONTA,
        RECUPERAR_SENHA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn( name = "fk_usuario",nullable = false)
    @ManyToOne
    private M_Usuario usuario;

    @Column(nullable = false,unique = true)
    private String ticket;

    @Column(nullable = false)
    private LocalDateTime validade;

    @Column(nullable = false)
    private Funcionalidade funcionalidade;

    @Override
    public String toString() {
        return "http://localhost:8080/"+getFuncionalidade().toString().toLowerCase(Locale.ROOT)+"/"+getTicket();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public LocalDateTime getValidade() {
        return validade;
    }

    public void setValidade(LocalDateTime validade) {
        this.validade = validade;
    }

    public M_Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(M_Usuario usuario) {
        this.usuario = usuario;
    }

    public Funcionalidade getFuncionalidade() {
        return funcionalidade;
    }

    public void setFuncionalidade(Funcionalidade funcionalidade) {
        this.funcionalidade = funcionalidade;
    }
}

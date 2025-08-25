package com.tcc.gelato.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Representa um ticket enviado ao e-mail do usuário para recuperar a senha
 */
@Entity
@Table(name = "ticket_recuperar_senha")
public class M_TicketRecuperarSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String ticket;

    @Column(nullable = false)
    private LocalDateTime validade;

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
}

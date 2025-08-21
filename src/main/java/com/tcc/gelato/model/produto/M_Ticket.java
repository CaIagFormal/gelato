package com.tcc.gelato.model.produto;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Transacao;
import com.tcc.gelato.model.M_Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * O pedido em sí composto de um pacote de {@link M_Compra}s.
 */
@Entity
@Table(name="ticket")
public class M_Ticket {

    public enum StatusCompra {
        CARRINHO,
        ENCAMINHADO,
        RECEBIDO,
        PREPARADO,
        ENTREGUE,
        CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 8, nullable = false)
    private String ticket;

    @Column(nullable = false)
    private StatusCompra status;

    @JoinColumn(name="fk_usuario",nullable = false)
    @ManyToOne
    private M_Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime horario_fornecido;

    @Column()
    private LocalDateTime horario_retirada;

    @JoinColumn(name="fk_pagamento")
    @ManyToOne
    private M_Transacao pagamento;

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

    public StatusCompra getStatus() {
        return status;
    }

    public void setStatus(StatusCompra status) {
        this.status = status;
    }

    public M_Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(M_Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getHorario_fornecido() {
        return horario_fornecido;
    }

    public void setHorario_fornecido(LocalDateTime horario_fornecido) {
        this.horario_fornecido = horario_fornecido;
    }

    public M_Transacao getPagamento() {
        return pagamento;
    }

    public void setPagamento(M_Transacao pagamento) {
        this.pagamento = pagamento;
    }

    public LocalDateTime getHorario_retirada() {
        return horario_retirada;
    }

    public void setHorario_retirada(LocalDateTime horario_retirada) {
        this.horario_retirada = horario_retirada;
    }
}

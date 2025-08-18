package com.tcc.gelato.model;

import com.tcc.gelato.model.produto.M_Produto;
import com.tcc.gelato.model.produto.M_Ticket;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A compra representa uma compra por um {@link M_Usuario} de um ou mais{@link M_Produto}
 */
@Entity
@Table(name="compra")
public class M_Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="fk_produto",nullable = false)
    @ManyToOne
    private M_Produto produto;

    @Column(precision = 10,scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private LocalDateTime horario;

    @JoinColumn(name="fk_ticket",nullable = false)
    @ManyToOne
    private M_Ticket ticket;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public M_Produto getProduto() {
        return produto;
    }

    public void setProduto(M_Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public M_Ticket getTicket() {
        return ticket;
    }

    public void setTicket(M_Ticket ticket) {
        this.ticket = ticket;
    }
}

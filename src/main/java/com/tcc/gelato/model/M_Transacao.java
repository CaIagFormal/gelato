package com.tcc.gelato.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Tabela que salva transações em Real entre cliente e vendedor
 */
@Entity
@Table(name="transacao")
public class M_Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    /**
     * Se a transação é o cliente dando saldo ao vendedor ou vice-versa
     */
    @Column(nullable = false)
    private boolean ao_vendedor;

    @JoinColumn(name="fk_cliente",nullable = false)
    @ManyToOne
    private M_Usuario cliente;

    @JoinColumn(name = "fk_vendedor",nullable = true)
    @ManyToOne
    private M_Usuario vendedor;

    @Column(nullable = false)
    private LocalDateTime horario_fornecido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean isAo_vendedor() {
        return ao_vendedor;
    }

    public void setAo_vendedor(boolean ao_vendedor) {
        this.ao_vendedor = ao_vendedor;
    }

    public M_Usuario getCliente() {
        return cliente;
    }

    public void setCliente(M_Usuario cliente) {
        this.cliente = cliente;
    }

    public M_Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(M_Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public LocalDateTime getHorario_fornecido() {
        return horario_fornecido;
    }

    public void setHorario_fornecido(LocalDateTime horario_fornecido) {
        this.horario_fornecido = horario_fornecido;
    }
}

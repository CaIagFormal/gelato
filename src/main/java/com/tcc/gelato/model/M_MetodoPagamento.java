package com.tcc.gelato.model;

import jakarta.persistence.*;

/**
 * Representa um método de pagamento a um vendedor para ser listado em uma página
 */
@Entity
@Table(name = "metodo_pagamento")
public class M_MetodoPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn( name = "fk_vendedor",nullable = false)
    @ManyToOne
    private M_Usuario vendedor;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String metodo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public M_Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(M_Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}

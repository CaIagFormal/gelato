package com.tcc.gelato.model;

import com.tcc.gelato.model.produto.M_Produto;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * A compra representa uma compra por um {@link M_Usuario} de um ou mais{@link com.tcc.gelato.model.produto.M_Produto}
 */
@Entity
@Table(name="compra")
public class M_Compra {

    public enum StatusCompra {
        CARRINHO,
        COMPRADA,
        CANCELADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="id_produto",nullable = false)
    @ManyToOne
    private M_Produto produto;

    @Column(nullable = false)
    private StatusCompra status;

    @Column(precision = 10,scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer qtd;

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

    public StatusCompra getStatus() {
        return status;
    }

    public void setStatus(StatusCompra status) {
        this.status = status;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }
}

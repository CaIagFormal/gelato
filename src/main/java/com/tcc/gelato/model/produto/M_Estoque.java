package com.tcc.gelato.model.produto;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Armazena quando, quem e quantos {@link M_Produto}s foram recebidos.
 */
@Entity
@Table(name="estoque")
public class M_Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="id_produto",nullable = false)
    @ManyToOne
    private M_Produto produto;

    @Column(nullable = false)
    private Integer qtd;

    @Column
    private LocalDateTime horario;

    @Column
    private String fornecedor;

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

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }
}

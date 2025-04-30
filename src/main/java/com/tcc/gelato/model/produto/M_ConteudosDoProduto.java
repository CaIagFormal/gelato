package com.tcc.gelato.model.produto;

import jakarta.persistence.*;


/**
 * Representa as informações nutricionais de um {@link M_Produto}
 */
@Entity
@Table(name="conteudos_do_produto")
public class M_ConteudosDoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="id_produto")
    @ManyToOne
    private M_Produto produto;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer qtd;

    @Column(nullable = false,length = 31)
    private String medida;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }
}

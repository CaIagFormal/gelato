package com.tcc.gelato.model.produto;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * O produto representa todas as mercadorias já vendidas.
 */
@Entity
@Table(name="produto")
public class M_Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;


    @Column(nullable = false, unique = true, length = 1023)
    private String descricao;

    @Column(nullable = false,precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false,length = 31)
    private String medida;

    @JoinColumn(name = "id_setor")
    @ManyToOne
    private M_Setor setor;

    @Column
    private String url_icone;

    @JoinColumn(name="id_produto")
    @OneToMany
    private List<M_AvisosDoProduto> avisos;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public M_Setor getSetor() {
        return setor;
    }

    public void setSetor(M_Setor setor) {
        this.setor = setor;
    }

    public String getUrl_icone() {
        return url_icone;
    }

    public void setUrl_icone(String url_icone) {
        this.url_icone = url_icone;
    }

    public List<M_AvisosDoProduto> getAvisos() {
        return avisos;
    }

    public void setAvisos(List<M_AvisosDoProduto> avisos) {
        this.avisos = avisos;
    }
}

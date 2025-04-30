package com.tcc.gelato.model.produto;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Lista quais {@link M_Aviso}s estão em {@link M_Produto}
 */
@Entity
@Table(name = "avisos_do_produto")
public class M_AvisosDoProduto {

    @Id
    @JoinColumn(name="id_produto")
    @ManyToOne
    private M_Produto produto;

    @Id
    @JoinColumn(name="id_aviso")
    @ManyToOne
    private M_Aviso aviso;

    public M_Produto getProduto() {
        return produto;
    }

    public void setProduto(M_Produto produto) {
        this.produto = produto;
    }

    public M_Aviso getAviso() {
        return aviso;
    }

    public void setAviso(M_Aviso aviso) {
        this.aviso = aviso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof M_AvisosDoProduto that)) return false;
        return Objects.equals(produto, that.produto) && Objects.equals(aviso, that.aviso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produto, aviso);
    }
}

package com.tcc.gelato.model.produto;

import com.tcc.gelato.model.M_Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Representa uma avaliação de um produto
 */

@Entity
@Table(name = "avalicao")
public class M_Avaliacao {

    public enum Avaliacao {
        ODIEI,
        NAO_GOSTEI("Não gostei"),
        QUESTIONAVEL("Qualidade questionável"),
        OK,
        BOM,
        GOSTOSO,
        DELICIOSO,
        ADOREI,
        AMEI,
        PERFEITO;

        private final String nome;

        Avaliacao(String nome) {
            this.nome = nome;
        }
        Avaliacao() {
            this.nome = name().substring(0, 1).toUpperCase() + name().substring(1);
        }
        @Override
        public String toString() {
            return nome;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn( name = "fk_usuario",nullable = false)
    @ManyToOne
    private M_Usuario usuario;

    @JoinColumn( name = "fk_produto",nullable = false)
    @ManyToOne
    private M_Produto produto;

    @Column(length = 1023, nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Avaliacao avaliacao;

    @Column(nullable = false)
    private LocalDateTime horario;

    @Column(nullable = false)
    private boolean anonima; // Se a avaliação é anônima

    @Column(nullable = false)
    private boolean publica; // Se a avaliação é pública

    @JoinColumn( name = "fk_vendedor_desabilitar")
    @ManyToOne
    private M_Usuario vendedor_desabilitar; // Representa o vendedor que desabilitou a avaliação caso esteja nulo a avaliação não foi desabilitada

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public M_Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(M_Usuario usuario) {
        this.usuario = usuario;
    }

    public M_Produto getProduto() {
        return produto;
    }

    public void setProduto(M_Produto produto) {
        this.produto = produto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    public boolean isAnonima() {
        return anonima;
    }

    public void setAnonima(boolean anonima) {
        this.anonima = anonima;
    }

    public boolean isPublica() {
        return publica;
    }

    public void setPublica(boolean publica) {
        this.publica = publica;
    }

    public M_Usuario getVendedor_desabilitar() {
        return vendedor_desabilitar;
    }

    public void setVendedor_desabilitar(M_Usuario vendedor_desabilitar) {
        this.vendedor_desabilitar = vendedor_desabilitar;
    }
}

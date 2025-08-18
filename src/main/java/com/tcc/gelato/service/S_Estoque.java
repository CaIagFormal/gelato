package com.tcc.gelato.service;

import com.tcc.gelato.model.produto.M_Estoque;
import com.tcc.gelato.model.produto.M_Produto;
import com.tcc.gelato.repository.produto.R_Estoque;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class S_Estoque {

    private final R_Estoque r_estoque;

    public S_Estoque(R_Estoque r_estoque) {
        this.r_estoque = r_estoque;
    }

    /**
     * Retorna o estoque atual de um {@link M_Produto} solicitado
     * @param m_produto {@link M_Produto} a ter o estoque contado
     * @return estoque atual
     */
    public Integer getEstoqueForProduto(M_Produto m_produto) {
        return r_estoque.getEstoqueForProduto(m_produto.getId());
    }


    /**
     * Confere se os parâmetros para adicionar {@link M_Estoque} são válidos
     * @param qtd Quantidade de {@link M_Produto} em {@link String}
     * @param id_produto ID do {@link M_Produto} em {@link String}
     * @return Validade dos parâmetros
     */
    public boolean checkAdicionarEstoqueValido(String qtd, String id_produto) {
        long id_val;
        try {
            id_val = Long.parseLong(id_produto);
            Integer.parseInt(qtd);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (id_val<1) {
            return false;
        }
        return true;
    }

    /**
     * Valida o estoque de um produto com qtd a mais de estoque
     * @param qtd Quantidade de estoque
     * @return Validade da quantidade
     */
    public boolean conferirValidadeDeEstoque(M_Produto m_produto,int qtd) {
        int estoque = r_estoque.getEstoqueForProduto(m_produto.getId());
        return (estoque+qtd>=0);
    }

    /**
     * Gera estoque baseado nos parâmetros
     * @param m_produto Produto a ter estoque adicionado
     * @param qtd Quantidade de estoque
     * @return
     */
    public M_Estoque gerarEstoque(M_Produto m_produto, int qtd) {
        M_Estoque m_estoque = new M_Estoque();

        m_estoque.setHorario(LocalDateTime.now());
        m_estoque.setProduto(m_produto);
        m_estoque.setQuantidade(qtd);

        return r_estoque.save(m_estoque);
    }
}

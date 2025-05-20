package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Produto;
import com.tcc.gelato.repository.R_Compra;
import com.tcc.gelato.repository.produto.R_Produto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Aplicação de regras de negócio de serviços relacionados a produtos
 */
@Service
public class S_Produto {

    private final R_Produto r_produto;

    private final R_Compra r_compra;

    public S_Produto(R_Produto r_produto, R_Compra r_compra) {
        this.r_produto = r_produto;
        this.r_compra = r_compra;
    }

    /**
     * @param id Id do produto
     * @return {@link com.tcc.gelato.model.produto.M_Produto} ou Null se der exceção
     */
    public M_Produto getProdutoById(long id) {
        M_Produto m_produto;
        try {
            m_produto = r_produto.getReferenceById(id);
        } catch (Exception e) {
            return null;
        }
        return m_produto;
    }

    /**
     * Valida se os parâmetros são adequados para usar no método {@link com.tcc.gelato.controller.C_Produto#adicionarAoCarrinho(String, String, HttpSession)}
     * @param qtd Quantidade do produto
     * @param id_produto ID do produto
     */
    public boolean checkAdicionarAoCarrinhoValido(String qtd, String id_produto) {
        try {
            Long.parseLong(id_produto);
            Integer.parseInt(qtd);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Gera uma compra para o carrinho
     * @param m_usuario O usuário que compra
     * @param m_produto O produto a ser comprado
     * @param qtd A quantidade de produto
     */
    public M_Compra gerarCompraDoCarrinho(M_Usuario m_usuario, M_Produto m_produto, Integer qtd) {
        M_Compra m_compra = new M_Compra();

        m_compra.setUsuario(m_usuario);
        m_compra.setProduto(m_produto);
        m_compra.setQtd(qtd);
        m_compra.setStatus(M_Compra.StatusCompra.CARRINHO);
        m_compra.setPreco(m_produto.getPreco());
        m_compra.setHorario(LocalDateTime.now());

        try {
            return r_compra.save(m_compra);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Pega a quantidade de compras definidas como carrinho de um usuário
     * @param m_usuario {@link M_Usuario}
     * @return Quantidade de {@link M_Compra}s no carrinho
     */
    public Integer getQtdComprasCarrinhoDeUsuario(M_Usuario m_usuario) {
        return r_compra.getQtdComprasCarrinhoDeUsuario(m_usuario.getId());
    }
}

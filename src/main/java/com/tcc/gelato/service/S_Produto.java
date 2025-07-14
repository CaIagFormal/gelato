package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Produto;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.repository.R_Compra;
import com.tcc.gelato.repository.produto.R_Estoque;
import com.tcc.gelato.repository.produto.R_Produto;
import com.tcc.gelato.repository.produto.R_Ticket;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Aplicação de regras de negócio de serviços relacionados a {@link M_Produto}
 */
@Service
public class S_Produto {

    private final R_Produto r_produto;

    private final R_Compra r_compra;

    private final R_Estoque r_estoque;

    private final R_Ticket r_ticket;

    public S_Produto(R_Produto r_produto, R_Compra r_compra, R_Estoque r_estoque, R_Ticket r_ticket) {
        this.r_produto = r_produto;
        this.r_compra = r_compra;
        this.r_estoque = r_estoque;
        this.r_ticket = r_ticket;
    }

    /**
     * @return Todos os {@link M_Produto}s disponíveis
     */
    public List<M_Produto> getProdutosDisponiveis() {
        return r_produto.getProdutosDisponiveis();
    }

    /**
     * @param id Id do produto
     * @return {@link com.tcc.gelato.model.produto.M_Produto} ou Null se der exceção
     */
    public M_Produto getProdutoById(long id) {
        Optional<M_Produto> m_produto;
        try {
            m_produto = r_produto.findById(id);
        } catch (Exception e) {
            return null;
        }
        if (m_produto.isEmpty()) {
            return null;
        }
        return m_produto.get();
    }

    /**
     * Valida se os parâmetros são adequados para usar no método {@link com.tcc.gelato.controller.C_Produto#adicionarAoCarrinho(String, String, HttpSession)}
     * @param qtd Quantidade do produto
     * @param id_produto ID do produto
     */
    public boolean checkAdicionarAoCarrinhoValido(String qtd, String id_produto) {
        Long id_val = 0l;
        Integer qtd_val = 0;
        try {
            Long.parseLong(id_produto);
            Integer.parseInt(qtd);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (id_val<1) {
            return false;
        }
        if (qtd_val<0) {
            return false;
        }
        return true;
    }


    /**
     * Gera uma compra para o carrinho
     * @param m_usuario O usuário que compra
     * @param m_ticket O ticket do usuário
     * @param m_produto O produto a ser comprado
     * @param qtd A quantidade de produto
     */
    public M_Compra gerarCompraDoCarrinho(M_Usuario m_usuario, M_Ticket m_ticket, M_Produto m_produto, Integer qtd) {
        M_Compra m_compra = new M_Compra();

        m_compra.setTicket(m_ticket);
        m_compra.setProduto(m_produto);
        m_compra.setQuantidade(qtd);
        m_compra.setPreco(m_produto.getPreco());
        m_compra.setHorario(LocalDateTime.now());

        try {
            return r_compra.save(m_compra);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Pega a quantidade de compras em um ticket
     * @param m_ticket {@link M_Ticket}
     * @return Quantidade de {@link M_Compra}s no ticket
     */
    public Integer getQtdComprasDeTicket(M_Ticket m_ticket) {
        return r_compra.getQtdComprasDeTicket(m_ticket.getId());
    }

    /**
     * Pega as compras de um ticket
     * @param m_ticket {@link M_Ticket
     * @return {@link M_Compra}s no carrinho
     */
    public List<M_Compra> getComprasDeTicket(M_Ticket m_ticket) {
        return r_compra.getComprasDeTicket(m_ticket.getId());
    }

    /**
     * Calcula o preço total de uma lista de compras
     * @param m_compras {@link M_Compra}s a serem totalizadas
     * @return Preço total
     */
    public BigDecimal getPrecoTotalDeCompras(List<M_Compra> m_compras) {
        BigDecimal total = new BigDecimal(0).setScale(2, RoundingMode.UNNECESSARY);
        for (M_Compra m_compra : m_compras) {
            total = total.add(m_compra.getPreco().multiply(new BigDecimal(m_compra.getQuantidade())));
        }
        return total;
    }

    /**
     * Retorna o estoque atual de um {@link com.tcc.gelato.model.produto.M_Produto} solicitado
     * @param m_produto {@link com.tcc.gelato.model.produto.M_Produto} a ter o estoque contado
     * @return estoque atual
     */
    public Integer getEstoqueForProduto(M_Produto m_produto) {
        return r_estoque.getEstoqueForProduto(m_produto.getId());
    }
}

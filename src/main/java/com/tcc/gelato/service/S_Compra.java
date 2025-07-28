package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Produto;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.repository.R_Compra;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class S_Compra {

    private final R_Compra r_compra;

    public S_Compra(R_Compra r_compra) {
        this.r_compra = r_compra;
    }

    /**
     * Valida se os parâmetros são adequados para usar no método {@link com.tcc.gelato.controller.C_Produto#adicionarAoCarrinho(String, String, HttpSession)}
     * @param qtd Quantidade do produto
     * @param id_produto ID do produto
     */
    public boolean checkAdicionarAoCarrinhoValido(String qtd, String id_produto) {
        long id_val;
        int qtd_val;
        try {
            id_val = Long.parseLong(id_produto);
            qtd_val = Integer.parseInt(qtd);
        } catch (Exception e) {
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
     * Valida se os parâmetros são adequados para usar no método {@link com.tcc.gelato.controller.C_Produto#removerDoCarrinho(String, HttpSession)}
     * @param id_compra ID da compra
     */
    public boolean checkRemoverDoCarrinhoValido(String id_compra) {
        long id_val;
        try {
            id_val = Long.parseLong(id_compra);
        } catch (Exception e) {
            return false;
        }
        if (id_val<1) {
            return false;
        }
        return true;
    }

    /**
     * Retorna uma {@link M_Compra} pelo ID
     * @param id ID da {@link M_Compra}}
     * @return {@link M_Compra}
     */
    public M_Compra getCompraById(long id) {
        Optional<M_Compra> m_compra = r_compra.findById(id);
        return m_compra.orElse(null);
    }

    /**
     * Remove uma {@link M_Compra} do banco de dados
     * @param m_compra A compra a ser apagada
     */
    public void removerCompra(M_Compra m_compra) {
        r_compra.delete(m_compra);
    }
}

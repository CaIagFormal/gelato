package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Estoque;
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

    public S_Produto(R_Produto r_produto) {
        this.r_produto = r_produto;
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
        return m_produto.orElse(null);
    }
}

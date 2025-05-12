package com.tcc.gelato.service;

import com.tcc.gelato.model.produto.M_Produto;
import com.tcc.gelato.repository.produto.R_Produto;
import org.springframework.stereotype.Service;

/**
 * Aplicação de regras de negócio de serviços relacionados a produtos
 */
@Service
public class S_Produto {

    private final R_Produto r_produto;

    public S_Produto(R_Produto r_produto) {
        this.r_produto = r_produto;
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
}

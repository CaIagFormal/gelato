package com.tcc.gelato.repository.produto;

import com.tcc.gelato.model.produto.M_Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Repositório da tabela {@link com.tcc.gelato.model.produto.M_Estoque}
 */
@Repository
public interface R_Estoque extends JpaRepository<M_Estoque,Long> {

    /**
     * Retorna o estoque atual de um {@link com.tcc.gelato.model.produto.M_Produto} solicitado
     * @param id_produto ID do {@link com.tcc.gelato.model.produto.M_Produto}
     * @return estoque atual
     */
    @Query(value = "with compra as (" +
            "select coalesce(sum(quantidade),0) as quantidade " +
            "from gelato.compra " +
            "where fk_produto = :ID_PRODUTO"+
            "), " +
            "estoque as (" +
            "select coalesce(sum(quantidade),0) as quantidade " +
            "from gelato.estoque " +
            "where fk_produto = :ID_PRODUTO" +
            ") " +
            "select estoque.quantidade-compra.quantidade "+
            "from estoque,compra "+
            "limit 1",nativeQuery = true)
    Integer getEstoqueForProduto(@Param("ID_PRODUTO") Long id_produto);
}

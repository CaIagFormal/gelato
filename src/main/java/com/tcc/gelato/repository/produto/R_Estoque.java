package com.tcc.gelato.repository.produto;

import com.tcc.gelato.model.produto.M_Estoque;
import com.tcc.gelato.model.produto.M_Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Repositório da tabela {@link M_Estoque}
 */
@Repository
public interface R_Estoque extends JpaRepository<M_Estoque,Long> {

    /**
     * Retorna o estoque atual de um {@link M_Produto} solicitado
     * {@link com.tcc.gelato.model.produto.M_Ticket.StatusCompra#CARRINHO} = 0,
     * {@link com.tcc.gelato.model.produto.M_Ticket.StatusCompra#CANCELADO} = 5
     * @param id_produto ID do {@link M_Produto}
     * @return estoque atual
     */
    @Query(value = "with compra as (" +
            "select coalesce(sum(c.quantidade),0) as quantidade " +
            "from gelato.compra c " +
            "join gelato.ticket t on c.fk_ticket=t.id " +
            "where c.fk_produto = :ID_PRODUTO and t.status <> 5 and t.status <> 0 "+
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

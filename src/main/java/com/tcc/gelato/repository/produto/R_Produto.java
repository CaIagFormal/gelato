package com.tcc.gelato.repository.produto;
import com.tcc.gelato.model.produto.M_Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório da tabela {@link M_Produto}
 */
@Repository
public interface R_Produto extends JpaRepository<M_Produto,Long> {
    @Query(value = "select * from gelato.produto where disponivel = true",nativeQuery = true)
    public List<M_Produto> getProdutosDisponiveis();

    @Query(value = "select * from gelato.produto order by disponivel desc",nativeQuery = true)
    public List<M_Produto> getProdutosVendedor();

    /**
     * Retorna a soma das quantidade dentro das compras de um produto em um ticket
     */
    @Query(value = "select coalesce(sum(quantidade),0) from gelato.compra where fk_produto = :PRODUTO and fk_ticket = :TICKET",nativeQuery = true)
    Integer getQtdDeProdutoEmTicket(@Param("PRODUTO") Long id_produto, @Param("TICKET") Long id_ticket);
}

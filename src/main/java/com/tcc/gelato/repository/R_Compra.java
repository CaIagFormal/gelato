package com.tcc.gelato.repository;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.produto.M_Produto;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.repository.produto.R_Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositório da tabela {@link M_Compra}
 */
public interface R_Compra extends JpaRepository<M_Compra, Long> {
    /**
     * Pega a quantidade de compras em um ticket
     * @param id_ticket ID do {@link M_Ticket} em questão
     * @return Quantidade de {@link M_Compra}s no ticket
     */
    @Query(value = "select count(*) from gelato.compra where fk_ticket=:ID_TICKET",nativeQuery = true)
    Integer getQtdComprasDeTicket(@Param("ID_TICKET") Long id_ticket);

    /**
     * obtêm uma compra de um produto específico num ticket
     * @param id_produto ID de um {@link M_Produto}
     * @param id_ticket ID de um {@link M_Ticket}
     * @param preco O preço do produto atualmente
     */
    @Query(value = "select * from gelato.compra where fk_ticket=:ID_TICKET and fk_produto=:ID_PRODUTO and preco=:PRECO limit 1",nativeQuery = true)
    M_Compra getCompraComProdutoEmTicket(@Param("ID_PRODUTO") Long id_produto,@Param("ID_TICKET") Long id_ticket,@Param("PRECO") BigDecimal preco);

    /**
     * Obtêm todas as compras onde a quantidade supera o estoque fornecido de um produto que estão no carrinho
     * @param id_produto ID do produto que terá as compras analisadas
     * @param estoque quantidade que a compra deve superar
     */
    @Query(value = "select c.* from gelato.compra c join gelato.ticket t on t.id = c.fk_ticket " +
            "where c.fk_produto=:ID_PRODUTO and c.quantidade > :ESTOQUE " +
            "and t.status = 0 and ("+ R_Ticket.check_ativo +")",nativeQuery = true)
    List<M_Compra> getComprasDeProdutoComQtdMaiorNoCarrinho(@Param("ID_PRODUTO") Long id_produto,@Param("ESTOQUE") Integer estoque);
}

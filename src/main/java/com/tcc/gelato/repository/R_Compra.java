package com.tcc.gelato.repository;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositório da tabela {@link M_Compra}
 */
public interface R_Compra extends JpaRepository<M_Compra, Long> {
    /**
     * Pega a quantidade de compras em um ticket
     * @param id_ticket ID do {@link com.tcc.gelato.model.produto.M_Ticket} em questão
     * @return Quantidade de {@link M_Compra}s no ticket
     */
    @Query(value = "select count(*) from gelato.compra where fk_ticket=:ID_TICKET",nativeQuery = true)
    Integer getQtdComprasDeTicket(@Param("ID_TICKET") Long id_ticket);

}

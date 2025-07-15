package com.tcc.gelato.repository.produto;

import com.tcc.gelato.model.produto.M_Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface R_Ticket extends JpaRepository<M_Ticket,Long> {

    /**
     * {@link com.tcc.gelato.model.produto.M_Ticket.StatusCompra#CANCELADO} = 4, altere se o valor mudar
     */
    String check_ativo = "(current_timestamp-horario_fornecido<('1 day')::interval) and "+
            "status <> 4";

    String latest = "order by horario_fornecido desc limit 1";

    /**
     * Pega o {@link M_Ticket} atual/ativo de um {@link com.tcc.gelato.model.M_Usuario}
     * @param id_usuario o ID do {@link com.tcc.gelato.model.M_Usuario}
     * @return {@link M_Ticket} mais recente, que é o ativo
     */
    @Query(value="select * from gelato.ticket where fk_usuario = :ID_USUARIO and "+check_ativo+" "+latest,nativeQuery = true)
    Optional<M_Ticket> getTicketAtivoDeUsuario(@Param("ID_USUARIO") Long id_usuario);

    /**
     * Pega o {@link M_Ticket} atual/ativo mais recente com a {@link String} fornecida
     * @param ticket String de ticket
     * @return {@link M_Ticket} mais recente/ativo com a {@link String}
     */
    @Query(value = "select * from gelato.ticket where ticket = :TICKET and "+check_ativo+" "+latest,nativeQuery = true)
    Optional<M_Ticket> getTicketAtivoByString(@Param("TICKET") String ticket);
}

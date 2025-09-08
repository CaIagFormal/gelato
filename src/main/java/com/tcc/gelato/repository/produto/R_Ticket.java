package com.tcc.gelato.repository.produto;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.view.M_ViewPedido;
import com.tcc.gelato.service.S_Ticket;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface R_Ticket extends JpaRepository<M_Ticket,Long> {

    /**
     * {@link M_Ticket.StatusCompra#CANCELADO} = 5
     * {@link M_Ticket.StatusCompra#CARRINHO} = 0,
     * {@link M_Ticket.StatusCompra#ENTREGUE} = 4, altere se o valor mudar
     */
    String check_ativo = "((current_date-cast(t.horario_fornecido as DATE)=0 or (t.status <> 0 and t.status <> 4)) and "+
            "t.status <> 5)";
    String check_pedido = "((current_date-cast(t.horario_fornecido as DATE)=0 or (t.status <> 4)) and t.status <> 0)";

    String latest = "order by t.horario_fornecido desc limit 1";

    /**
     * Pega o {@link M_Ticket} atual/ativo de um {@link M_Usuario}
     * @param id_usuario o ID do {@link M_Usuario}
     * @return {@link M_Ticket} mais recente, que é o ativo
     */
    @Query(value="select * from gelato.ticket t where t.fk_usuario = :ID_USUARIO and "+check_ativo+" "+latest,nativeQuery = true)
    Optional<M_Ticket> getTicketAtivoDeUsuario(@Param("ID_USUARIO") Long id_usuario);

    /**
     * Pega o {@link M_Ticket} atual/ativo mais recente com a {@link String} fornecida
     * @param ticket String de ticket
     * @return {@link M_Ticket} mais recente/ativo com a {@link String}
     */
    @Query(value = "select * from gelato.ticket t where t.ticket = :TICKET and "+check_ativo+" "+latest,nativeQuery = true)
    Optional<M_Ticket> getTicketAtivoByString(@Param("TICKET") String ticket);

    /**
     * Seleciona todos os tickets considerados descartáveis por {@link S_Ticket#apagarTicketsDescartaveis()}
     * {@link M_Ticket.StatusCompra#CARRINHO}==0
     */
    @Query(value = "select * from gelato.ticket t where t.status=0 and not( "+check_ativo+" )",nativeQuery = true)
    List<M_Ticket> selecionarTicketsDescartaveis();

    /**
     * Pega as compras de um ticket em order cronológica do mais recente ao mais antigo
     * @param id_ticket ID do {@link M_Ticket} em questão
     * @return {@link M_Compra}s no ticket
     */
    @Query(value = "select * from gelato.compra where fk_ticket=:ID_TICKET order by horario desc",nativeQuery = true)
    List<M_Compra> getComprasDeTicket(@Param("ID_TICKET") Long id_ticket);

    /**
     * Pega todos os pedidos ativos no momento
     */
    @Query(value = "select t.id as id_ticket," +
            "t.ticket as ticket," +
            "c.nome as nome_cliente," +
            "p.horario_fornecido as horario_encaminhado," +
            "t.horario_retirada_planejado," +
            "p.valor as preco," +
            "t.observacao," +
            "t.status as status_id," +
            "(t.horario_retirada_planejado-current_timestamp) as contagem_retirada,"+
            "current_timestamp>t.horario_retirada_planejado as contagem_negativa,"+
            "t.horario_retirada_final as horario_retirada_final "+
            "from gelato.ticket t " +
            "join gelato.usuario c on c.id = t.fk_usuario left join gelato.transacao p on p.id = t.fk_pagamento " +
            "where "+check_pedido+" order by t.status,t.horario_retirada_final,t.horario_retirada_planejado desc",nativeQuery = true)
    List<M_ViewPedido> getPedidos();

    /**
     * Obtêm um pedido a partir de um ticket
     * @param ticket ID do ticket
     */
    @Query(value = "select t.id as id_ticket," +
            "t.ticket as ticket," +
            "c.nome as nome_cliente," +
            "p.horario_fornecido as horario_encaminhado," +
            "t.horario_retirada_planejado," +
            "p.valor as preco," +
            "t.observacao," +
            "t.status as status_id," +
            "(t.horario_retirada_planejado-current_timestamp) as contagem_retirada,"+
            "current_timestamp>t.horario_retirada_planejado as contagem_negativa,"+
            "t.horario_retirada_final as horario_retirada_final "+
            "from gelato.ticket t " +
            "join gelato.usuario c on c.id = t.fk_usuario left join gelato.transacao p on p.id = t.fk_pagamento " +
            "where t.id=:TICKET", nativeQuery = true)
    M_ViewPedido getPedidoFromTicket(@Param("TICKET") Long ticket);
}

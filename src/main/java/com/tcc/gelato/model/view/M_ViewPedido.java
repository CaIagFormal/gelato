package com.tcc.gelato.model.view;

import com.tcc.gelato.model.produto.M_Ticket;
import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.persistence.Convert;
import org.postgresql.util.PGInterval;
import org.springframework.beans.factory.annotation.Value;

public interface M_ViewPedido {
    Long getId_ticket();
    String getTicket();
    String getNome_cliente();
    LocalDateTime getHorario_encaminhado();
    LocalDateTime getHorario_retirada_planejado();
    LocalDateTime getHorario_retirada_final();
    BigDecimal getPreco();
    String getObservacao();
    Short getStatus_id();

    default M_Ticket.StatusCompra getStatus() {
        return M_Ticket.StatusCompra.index(getStatus_id());
    }

    PGInterval getContagem_retirada();
    Boolean getContagem_negativa();
}

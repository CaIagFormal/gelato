package com.tcc.gelato.model.view;

import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public interface M_ViewPedido {
    Long getId_ticket();
    String getTicket();
    String getNome_cliente();
    LocalDateTime getHorario_encaminhado();
    LocalDateTime getHorario_retirada();
    BigDecimal getPreco();
    String getObservacao();
    Short getStatus_id();

    @Column(columnDefinition = "interval")
    Duration getContagem_retirada();
}

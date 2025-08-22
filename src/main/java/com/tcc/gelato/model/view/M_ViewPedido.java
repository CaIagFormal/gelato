package com.tcc.gelato.model.view;

import java.math.BigDecimal;
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
    String getContagem_retirada();
}

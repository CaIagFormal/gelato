package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Transacao;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.repository.R_Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Serviço para lidar com regras de negócio do controller {@link com.tcc.gelato.controller.C_Transacao}
 */

@Service
public class S_Transacao {

    private final R_Transacao r_transacao;

    public S_Transacao(R_Transacao r_transacao) {
        this.r_transacao = r_transacao;
    }

    /**
     * Confere os parâmetros de funções relacionadas ao saldo
     * @param cliente {@link com.tcc.gelato.model.M_Usuario.Cargo#CLIENTE} a ter o saldo alterado
     * @param qtd {@link Integer}
     * @return
     */
    public boolean checkParamAlterarSaldoValido(String cliente, String qtd) {
        BigDecimal val_qtd;
        try {
            val_qtd = new BigDecimal(qtd);
        } catch (Exception e) {
            return false;
        }
        if (val_qtd.compareTo(BigDecimal.ZERO)==0) {
            return false;
        }
        if (cliente.isBlank()) {
            return false;
        }
        return true;
    }

    public M_Transacao alterarSaldo(BigDecimal qtd, M_Usuario vendedor, M_Usuario cliente) {
        M_Transacao m_transacao = new M_Transacao();
        m_transacao.setHorario_fornecido(LocalDateTime.now());

        m_transacao.setAo_vendedor(qtd.compareTo(BigDecimal.ZERO)<0);
        m_transacao.setValor(qtd.abs());

        m_transacao.setVendedor(vendedor);
        m_transacao.setCliente(cliente);
        return r_transacao.save(m_transacao);
    }
}

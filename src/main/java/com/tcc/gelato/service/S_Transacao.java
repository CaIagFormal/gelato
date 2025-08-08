package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Transacao;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.repository.R_Transacao;
import jakarta.servlet.http.HttpSession;
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

    /**
     * Altera o saldo de um cliente pela quantidade dada, sendo que o sinal indica a direção da operação
     * @param qtd Quantidade a ser alterada
     * @param vendedor Vendedor que realizou a operação
     * @param cliente Cliente a ter o saldo alterado
     * @return Transação representante
     */
    public M_Transacao alterarSaldo(BigDecimal qtd, M_Usuario vendedor, M_Usuario cliente) {
        M_Transacao m_transacao = new M_Transacao();
        m_transacao.setHorario_fornecido(LocalDateTime.now());

        m_transacao.setAo_vendedor(qtd.compareTo(BigDecimal.ZERO)<0);
        m_transacao.setValor(qtd.abs());

        m_transacao.setVendedor(vendedor);
        m_transacao.setCliente(cliente);
        return r_transacao.save(m_transacao);
    }

    /**
     * Obtêm o saldo de um cliente
     * @param cliente {@link M_Usuario.Cargo#CLIENTE} a inspecionar o saldo
     * @return Saldo do cliente
     */
    public BigDecimal getSaldoDeCliente(M_Usuario cliente) {
        return r_transacao.getSaldoDeCliente(cliente.getId());
    }

    /**
     * Valida a quantidade de saldo a ser alterado em {@link com.tcc.gelato.controller.C_Transacao#alterarSaldo(HttpSession, String, String)}
     * @param qtd Quantidade para alterar o saldo
     * @param saldo_cliente Saldo do cliente no momento, ultilize {@link S_Transacao#getSaldoDeCliente(M_Usuario)} para alterar
     * @return Validade da operação
     */
    public boolean validarQtdAlterarSaldo(BigDecimal qtd,BigDecimal saldo_cliente) {
        return saldo_cliente.add(qtd).compareTo(BigDecimal.ZERO) > -1;
    }
}

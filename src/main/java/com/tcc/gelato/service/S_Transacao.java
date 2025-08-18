package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Transacao;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.controller.C_Transacao;
import com.tcc.gelato.repository.R_Transacao;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço para lidar com regras de negócio do controller {@link C_Transacao}
 */

@Service
public class S_Transacao {

    private final R_Transacao r_transacao;

    public static BigDecimal psql_big_decimal_limit = new BigDecimal("100000000.00");

    public S_Transacao(R_Transacao r_transacao) {
        this.r_transacao = r_transacao;
    }

    /**
     * Confere os parâmetros de funções relacionadas ao saldo
     * @param cliente {@link M_Usuario.Cargo#CLIENTE} a ter o saldo alterado
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
     * Valida a quantidade de saldo a ser alterado em {@link C_Transacao#alterarSaldo(HttpSession, String, String)}
     * @param qtd Quantidade para alterar o saldo
     * @param saldo_cliente Saldo do cliente no momento, ultilize {@link S_Transacao#getSaldoDeCliente(M_Usuario)} para alterar
     * @return Validade da operação
     */
    public boolean validarQtdAlterarSaldo(BigDecimal qtd,BigDecimal saldo_cliente) {
        BigDecimal total = saldo_cliente.add(qtd);
        if (qtd.abs().compareTo(psql_big_decimal_limit)> -1) return false;
        if (total.compareTo(psql_big_decimal_limit)> -1) return false; // Limite do PSQL
        return total.compareTo(BigDecimal.ZERO) > -1;
    }

    /**
     * @param cliente {@link M_Usuario}
     * @return Transações de um cliente
     */
    public List<M_Transacao> getTransacoesDeCliente(M_Usuario cliente) {
        return r_transacao.getTransacoesDeCliente(cliente.getId());
    }

    /**
     * Para {@link C_Transacao#inspecionarTransacoes(HttpSession, String)}
     * @param m_transacoes
     * @return Prepara as mensagens de um histórico de transação;
     */
    public List<List<String>> prepararMensagemTransacao(List<M_Transacao> m_transacoes) {

        List<List<String>> mensagem = new ArrayList<>();

        for (M_Transacao m_transacao: m_transacoes) {
            List<String> str_transcacao = new ArrayList<>();
            str_transcacao.add(m_transacao.getValor().toString());
            str_transcacao.add(m_transacao.isAo_vendedor()?"Ao vendedor":"Ao cliente");
            str_transcacao.add(m_transacao.getCliente().getNome());
            str_transcacao.add(m_transacao.getVendedor().getNome());
            str_transcacao.add(m_transacao.getHorario_fornecido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

            mensagem.add(str_transcacao);
        }
        return mensagem;
    }
}

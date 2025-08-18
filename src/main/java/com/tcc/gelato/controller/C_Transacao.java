package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Transacao;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.servidor.M_Resposta;
import com.tcc.gelato.model.servidor.M_RespostaObjeto;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.service.S_Cadastro;
import com.tcc.gelato.service.S_Cargo;
import com.tcc.gelato.service.S_Transacao;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

/**
 * Funcionalidades relacionadas a classe {@link M_Transacao}
 */
@Controller
public class C_Transacao {

    private final S_Cargo s_cargo;
    private final S_Transacao s_transacao;
    private final S_Cadastro s_cadastro;

    public C_Transacao(S_Cargo s_cargo, S_Transacao s_transacao, S_Cadastro s_cadastro) {
        this.s_cargo = s_cargo;
        this.s_transacao = s_transacao;
        this.s_cadastro = s_cadastro;
    }

    /**
     * @param session Sessão de usuário parar validar se é vendedor
     * @return Tela de gerenciar saldo de clientes com 5 funcionalidades
     */
    @GetMapping(path = "gerir_saldo")
    public String getGerirSaldo(HttpSession session) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);

        if (!s_cargo.validarVendedor(m_usuario)) {
            return "redirect:/";
        }

        return "vendedor/gerir_saldo";
    }

    /**
     *
     * @param session Sessão do vendedor
     * @param str_cliente Nome ou e-mail do cliente
     * @param str_qtd Quantidade de saldo para ser removida/adicionada
     * @return {@link M_RespostaTexto}
     */
    @PostMapping(path = "alterar_saldo")
    @ResponseBody
    public M_Resposta alterarSaldo(HttpSession session, @RequestParam("cliente") String str_cliente, @RequestParam("qtd") String str_qtd) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);

        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();

        if (!s_cargo.validarVendedor(m_usuario)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Você não está cadastrado como um vendedor.");
            return m_respostaTexto;
        }

        if (!s_transacao.checkParamAlterarSaldoValido(str_cliente,str_qtd)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Parâmetros inválidos.");
            return m_respostaTexto;
        }

        M_Usuario cliente = s_cadastro.getUsuarioByNomeOrEmail(str_cliente);
        if (!s_cargo.validarCliente(cliente)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Usuário inserido não é cliente.");
            return m_respostaTexto;
        }

        BigDecimal qtd = new BigDecimal(str_qtd);
        BigDecimal saldo_cliente = s_transacao.getSaldoDeCliente(cliente);
        if (!s_transacao.validarQtdAlterarSaldo(qtd,saldo_cliente)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Saldo inválido.");
            return m_respostaTexto;
        }

        M_Transacao m_transacao = s_transacao.alterarSaldo(qtd,m_usuario,cliente);

        if (m_transacao == null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("O servidor não consegui se comunicar com o banco de dados.");
            return m_respostaTexto;
        }

        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem("Foi "+(m_transacao.isAo_vendedor()?"removido":"adicionado")+" R$"+m_transacao.getValor()+" para o saldo de "+cliente.getNome());
        return m_respostaTexto;
    }

    /**
     * Esvazia o saldo do {@link M_Usuario.Cargo#CLIENTE} fornecido
     * @param session Sessão do vendedor
     * @param str_cliente String contendo o nome de um cliente
     * @return
     */
    @PostMapping(path = "esvaziar_saldo")
    @ResponseBody
    public M_Resposta esvaziarSaldo(HttpSession session, @RequestParam("cliente") String str_cliente) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);

        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();

        if (!s_cargo.validarVendedor(m_usuario)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Você não está cadastrado como um vendedor.");
            return m_respostaTexto;
        }

        if (!s_transacao.checkParamAlterarSaldoValido(str_cliente,"1")) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Parâmetros inválidos.");
            return m_respostaTexto;
        }

        M_Usuario cliente = s_cadastro.getUsuarioByNomeOrEmail(str_cliente);
        if (!s_cargo.validarCliente(cliente)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Usuário inserido não é cliente.");
            return m_respostaTexto;
        }

        BigDecimal qtd = s_transacao.getSaldoDeCliente(cliente).negate();
        if (qtd.compareTo(BigDecimal.ZERO)==0) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("O saldo do cliente está limpo já.");
            return m_respostaTexto;
        }
        if (!s_transacao.validarQtdAlterarSaldo(qtd,qtd.negate())) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("O cliente têm um saldo alto demais para apagar, por favor chame um técnico.");
            return m_respostaTexto;
        }

        M_Transacao m_transacao = s_transacao.alterarSaldo(qtd,m_usuario,cliente);

        if (m_transacao == null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("O servidor não consegui se comunicar com o banco de dados.");
            return m_respostaTexto;
        }

        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem("Foi "+(m_transacao.isAo_vendedor()?"removido":"adicionado")+" R$"+m_transacao.getValor()+" para o saldo de "+cliente.getNome());
        return m_respostaTexto;

    }


    @PostMapping(path="/inspecionar_saldo")
    @ResponseBody
    public M_Resposta inspecionarSaldo(HttpSession session, @RequestParam("cliente") String str_cliente) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);

        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();

        if (!s_cargo.validarVendedor(m_usuario)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Você não está cadastrado como um vendedor.");
            return m_respostaTexto;
        }

        M_Usuario cliente = s_cadastro.getUsuarioByNomeOrEmail(str_cliente);
        if (!s_cargo.validarCliente(cliente)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Usuário inserido não é cliente.");
            return m_respostaTexto;
        }
        BigDecimal saldo = s_transacao.getSaldoDeCliente(cliente);
        if (saldo==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Não foi possível obter o saldo de "+str_cliente);
            return m_respostaTexto;
        }

        m_respostaTexto.setMensagem(saldo.toString());
        m_respostaTexto.setSucesso(true);
        return m_respostaTexto;
    }

    @PostMapping(path="/inspecionar_transacoes")
    @ResponseBody
    public M_Resposta inspecionarTransacoes(HttpSession session, @RequestParam("cliente") String str_cliente) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);

        M_Resposta m_resposta;

        if (!s_cargo.validarVendedor(m_usuario)) {
            m_resposta = new M_RespostaTexto();
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Você não está cadastrado como um vendedor.");
            return m_resposta;
        }

        M_Usuario cliente = s_cadastro.getUsuarioByNomeOrEmail(str_cliente);
        if (!s_cargo.validarCliente(cliente)) {
            m_resposta = new M_RespostaTexto();
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Usuário inserido não é cliente.");
            return m_resposta;
        }
        List<M_Transacao> m_transacoes = s_transacao.getTransacoesDeCliente(cliente);
        if (m_transacoes==null) {
            m_resposta = new M_RespostaTexto();
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Não foi possível obter as transações de "+str_cliente);
            return m_resposta;
        }

        if (m_transacoes.size()==0) {
            m_resposta = new M_RespostaTexto();
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Não há transações feitas com "+str_cliente);
            return m_resposta;
        }

        m_resposta = new M_RespostaObjeto();
        m_resposta.setMensagem(s_transacao.prepararMensagemTransacao(m_transacoes));
        m_resposta.setSucesso(true);
        return m_resposta;
    }
}

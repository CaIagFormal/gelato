package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Transacao;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.servidor.M_Resposta;
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

/**
 * Funcionalidades relacionadas a classe {@link com.tcc.gelato.model.M_Transacao}
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
     * @return {@link M_Resposta}
     */
    @PostMapping(path = "alterar_saldo")
    @ResponseBody
    public M_Resposta alterarSaldo(HttpSession session, @RequestParam("cliente") String str_cliente, @RequestParam("qtd") String str_qtd) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao(session);

        M_Resposta m_resposta = new M_Resposta();

        if (!s_cargo.validarVendedor(m_usuario)) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Você não está cadastrado como um vendedor.");
            return m_resposta;
        }

        if (!s_transacao.checkParamAlterarSaldoValido(str_cliente,str_qtd)) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Parâmetros inválidos.");
            return m_resposta;
        }

        M_Usuario cliente = s_cadastro.getUsuarioByNomeOrEmail(str_cliente);
        if (!s_cargo.validarCliente(cliente)) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("Usuário inserido não é cliente.");
            return m_resposta;
        }

        BigDecimal qtd = new BigDecimal(str_qtd);

        M_Transacao m_transacao = s_transacao.alterarSaldo(qtd,m_usuario,cliente);

        if (m_transacao == null) {
            m_resposta.setSucesso(false);
            m_resposta.setMensagem("O servidor não consegui se comunicar com o banco de dados.");
            return m_resposta;
        }

        m_resposta.setSucesso(true);
        m_resposta.setMensagem("Foi "+(m_transacao.isAo_vendedor()?"removido":"adicionado")+" R$"+m_transacao.getValor()+" para o saldo de "+cliente.getNome());
        return m_resposta;
    }
}

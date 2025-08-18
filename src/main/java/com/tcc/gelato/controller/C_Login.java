package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.servidor.M_Navbar;
import com.tcc.gelato.model.servidor.M_NavbarCliente;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.mvc.service.*;
import com.tcc.gelato.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class C_Login {

    private final S_Login s_login;
    private final S_Compra s_compra;

    private final S_Cargo s_cargo;

    private final S_Ticket s_ticket;

    private final S_Transacao s_transacao;

    public C_Login(S_Login s_login, S_Compra s_compra, S_Cargo s_cargo, S_Ticket s_ticket, S_Transacao s_transacao) {
        this.s_login = s_login;
        this.s_compra = s_compra;
        this.s_cargo = s_cargo;
        this.s_ticket = s_ticket;
        this.s_transacao = s_transacao;
    }

    /**
     * Essa tela atua para todos os atores
     * @return Tela de login ou redireciona para {@link C_Inicio#redirecionar(HttpSession)} se já estiver logado
     */
    @GetMapping(path="/login")
    public String getLogin(HttpSession session){
        if (session.getAttribute("usuario")!=null) {
            return "redirect:/";
        }
        return "visitante/login";
    }

    /**
     * Faz login do usuário se os dados forem fornecidos corretamente
     * @param nome Nome ou e-mail do usuário
     * @param senha Senha do usuário
     */
    @PostMapping(path = "/login")
    @ResponseBody
    public M_RespostaTexto loginUsuario(@RequestParam String nome,
                                   @RequestParam String senha,
                                   HttpSession session) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();
        M_Usuario m_usuario = s_login.loginUsuario(nome, senha);
        if (m_usuario==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Nome ou senha não estão corretos...");
            return m_respostaTexto;
        }
        session.setAttribute("usuario",m_usuario);

        if (s_cargo.validarCliente(m_usuario)) {
            M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);

            // Criar navbar do cliente
            M_Navbar m_navbar = new M_NavbarCliente();
            session.setAttribute("m_navbar",m_navbar);

            // Definir quantidade de itens no carrinho
            s_cargo.navClienteSetQtdCompras(session,s_compra.getQtdComprasDeTicket(m_ticket));
            s_cargo.navClienteSetSaldo(session,s_transacao.getSaldoDeCliente(m_usuario));
        }

        m_respostaTexto.setMensagem("Fez login como "+m_usuario.getNome()+" ["+m_usuario.getCargo().toString()+"]");
        m_respostaTexto.setSucesso(true);
        return m_respostaTexto;
    }
}

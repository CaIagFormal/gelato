package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_AcessoViaUrl;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.service.S_AcessoViaUrl;
import com.tcc.gelato.service.S_Cadastro;
import com.tcc.gelato.service.S_JavaMailSender;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

@Controller
public class C_Cadastro {

    private final S_Cadastro s_cadastro;
    private final S_JavaMailSender s_javaMailSender;
    private final S_AcessoViaUrl s_acessoViaUrl;

    public C_Cadastro(S_Cadastro s_cadastro, S_JavaMailSender s_javaMailSender, S_AcessoViaUrl s_acessoViaUrl) {
        this.s_cadastro = s_cadastro;
        this.s_javaMailSender = s_javaMailSender;
        this.s_acessoViaUrl = s_acessoViaUrl;
    }

    /**
     * @return Tela de cadastro do cliente ou redireciona para {@link C_Inicio#redirecionar(HttpSession)} se já estiver logado
     */
    @GetMapping(path="/cadastro")
    public String getCadastroCliente(HttpSession session){
        return "visitante/cadastro";
    }

    /**
     * Cadastra um usuário se foi fornecido com os dados corretamente.
     * Redireciona para {@link C_Login#getLogin(HttpSession)}
     * @param nome Nome do cliente
     * @param senha Senha do cliente
     * @param conf_senha Confirmação da senha do cliente
     * @param email E-mail do cliente
     * @param telefone Telefone do cliente
     */
    @PostMapping(path="/cadastrar")
    @ResponseBody
    @Transactional
    public M_RespostaTexto cadastrarCliente(
            @RequestParam("nome") String nome,
            @RequestParam("senha") String senha,
            @RequestParam("conf_senha") String conf_senha,
            @RequestParam("email") String email,
            @RequestParam("telefone") String telefone) {
        M_RespostaTexto m_respostaTexto = s_cadastro.validarCadastroCliente(nome, senha, conf_senha, email, telefone);
        if (!m_respostaTexto.isSucesso()) {
            return m_respostaTexto;
        }
        M_Usuario m_usuario;
        try {
            m_usuario = s_cadastro.criarCadastroCliente(nome, senha, email, telefone);

            if (m_usuario==null) {
                m_respostaTexto.setMensagem("Parâmetros já foram utilizados (email,nome)");
                m_respostaTexto.setSucesso(false);
                return m_respostaTexto;
            }
        } catch (Exception e) {
            m_respostaTexto.setMensagem("Erro ao acessar banco de dados");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }

       // Tenta criar acesso via e-mail para recurso de verificar conta
        M_AcessoViaUrl m_acessoViaUrl = s_acessoViaUrl.criarAcesso(M_AcessoViaUrl.Funcionalidade.VERIFICAR_CONTA,m_usuario);
       if (m_acessoViaUrl==null) {
           s_cadastro.deleteUsuario(m_usuario);
           m_respostaTexto.setMensagem("Erro criando acesso à verificação de e-mail...");
           m_respostaTexto.setSucesso(false);
           return m_respostaTexto;
       }
       Context context = new Context();

       context.setVariable("v_acesso",m_acessoViaUrl);

       try {
           s_javaMailSender.enviarHTML(m_usuario.getEmail(),"Verificar conta","pv/email_cadastro",context);
       } catch (Exception e) {
           s_acessoViaUrl.deleteAcesso(m_acessoViaUrl);
           s_cadastro.deleteUsuario(m_usuario);
           m_respostaTexto.setMensagem("Erro enviando e-mail de verificação... (O email inserido é válido?)");
           m_respostaTexto.setSucesso(false);
           return m_respostaTexto;
       }

       m_respostaTexto.setMensagem("Conta cadastrada com sucesso. Verifique seu e-mail,<br>confira spam caso não esteja na sua caixa de entrada");
       return m_respostaTexto;
    }

    @GetMapping("/verificar_conta/{ticket}")
    public String verificarConta(@PathVariable("ticket") String ticket, Model model) {
        M_AcessoViaUrl m_acessoViaUrl = s_acessoViaUrl.getAcessoByTicketOfType(ticket,M_AcessoViaUrl.Funcionalidade.VERIFICAR_CONTA);
        if (m_acessoViaUrl==null) {
            return "redirect:/";
        }
        M_Usuario m_usuario = s_cadastro.verificarUsuario(m_acessoViaUrl.getUsuario());
        if (m_usuario==null) {
            return "redirect:/";
        }
        model.addAttribute("v_usuario",m_usuario);
        return "visitante/conta_verificada";
    }
}

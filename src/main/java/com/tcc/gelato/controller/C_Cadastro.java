package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_AcessoViaUrl;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.servidor.M_RespostaObjeto;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.service.S_AcessoViaUrl;
import com.tcc.gelato.service.S_Cadastro;
import com.tcc.gelato.service.S_Cargo;
import com.tcc.gelato.service.S_JavaMailSender;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

@Controller
public class C_Cadastro {

    private final S_Cadastro s_cadastro;
    private final S_JavaMailSender s_javaMailSender;
    private final S_AcessoViaUrl s_acessoViaUrl;
    private final S_Cargo s_cargo;

    public C_Cadastro(S_Cadastro s_cadastro, S_JavaMailSender s_javaMailSender, S_AcessoViaUrl s_acessoViaUrl, S_Cargo s_cargo) {
        this.s_cadastro = s_cadastro;
        this.s_javaMailSender = s_javaMailSender;
        this.s_acessoViaUrl = s_acessoViaUrl;
        this.s_cargo = s_cargo;
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

    /**
     * Tela acessada via URL para Verificar a conta
     */
    @GetMapping("/verificar_conta/{ticket}")
    public String verificarConta(@PathVariable("ticket") String ticket, Model model) {
        M_AcessoViaUrl m_acessoViaUrl = s_acessoViaUrl.getAcessoByTicketOfType(ticket,M_AcessoViaUrl.Funcionalidade.VERIFICAR_CONTA);
        if (m_acessoViaUrl==null) {
            return "redirect:/";
        }
        s_acessoViaUrl.deleteAcesso(m_acessoViaUrl);
        M_Usuario m_usuario = s_cadastro.verificarUsuario(m_acessoViaUrl.getUsuario());
        if (m_usuario==null) {
            return "redirect:/";
        }
        model.addAttribute("v_usuario",m_usuario);
        return "visitante/conta_verificada";
    }

    /**
     * Tela para enviar um e-mail que pode ser acessado para verificar a conta
     */
    @GetMapping("/recuperar_senha")
    public String getRecuperarSenha(Model model,HttpSession session) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao();
        model.addAttribute("usuario",m_usuario);
        s_cargo.session_to_model_navbar(model,session);

        return "recuperar_senha";
    }

    /**
     * Envia o e-mail para o cliente
     * @param nome Nome ou e-mail do cliente
     */
    @PostMapping("/recuperar_senha")
    @ResponseBody
    public M_RespostaTexto recuperarSenha(@RequestParam("nome") String nome) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();

        M_Usuario m_usuario;
        if (!s_cadastro.validarGetUsuarioByNomeOrEmail(nome)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Cadastro inválido");
            return m_respostaTexto;
        }
        try {
            m_usuario = s_cadastro.getUsuarioByNomeOrEmail(nome);
            if (m_usuario==null) {
                m_respostaTexto.setSucesso(false);
                m_respostaTexto.setMensagem("Cadastro não encontrado.");
                return m_respostaTexto;
            }
        } catch (Exception e) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Erro tentando encontrar cadastro.");
            return m_respostaTexto;
        }

        M_AcessoViaUrl m_acessoViaUrl = s_acessoViaUrl.criarAcesso(M_AcessoViaUrl.Funcionalidade.RECUPERAR_SENHA,m_usuario);
        if (m_acessoViaUrl==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Falha ao criar acesso");
            return m_respostaTexto;
        }

        Context context = new Context();
        context.setVariable("v_acesso",m_acessoViaUrl);

        try {
            s_javaMailSender.enviarHTML(m_usuario.getEmail(),"Recuperar Senha","pv/email_recuperar_senha",context);
        } catch (Exception e) {
            s_acessoViaUrl.deleteAcesso(m_acessoViaUrl);
            m_respostaTexto.setMensagem("Erro enviando e-mail...");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }
        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem("E-mail enviado para "+m_usuario.getEmail()+", confira sua caixa de entrada e spam.");
       return m_respostaTexto;
    }
    /**
     * Tela acessada via URL para Verificar a conta
     */
    @GetMapping("/redefinir_senha/{ticket}")
    public String getRedefinirSenha(@PathVariable("ticket") String ticket, HttpSession session, Model model) {
        M_AcessoViaUrl m_acessoViaUrl = s_acessoViaUrl.getAcessoByTicketOfType(ticket,M_AcessoViaUrl.Funcionalidade.RECUPERAR_SENHA);
        if (m_acessoViaUrl==null) {
            return "redirect:/";
        }
        if (m_acessoViaUrl.getUsuario()==null) {
            return "redirect:/";
        }
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao();
        s_cargo.session_to_model_navbar(model,session);

        model.addAttribute("usuario",m_usuario);
        model.addAttribute("v_acesso",m_acessoViaUrl);
        return "redefinir_senha";
    }
    /**
     * Redefine a senha de um usuário por meio de um acesso
     */
    @PostMapping("/redefinir_senha/{acesso}")
    @ResponseBody
    public M_RespostaTexto redefinirSenha(@RequestParam("senha") String senha,
                                 @RequestParam("conf_senha") String conf_senha,
                                 @PathVariable("acesso") String acesso) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();
        m_respostaTexto.setMensagem("");
        M_AcessoViaUrl m_acessoViaUrl = s_acessoViaUrl.getAcessoByTicketOfType(acesso,M_AcessoViaUrl.Funcionalidade.RECUPERAR_SENHA);

        if (m_acessoViaUrl==null) {
            m_respostaTexto.setMensagem("Acesso inválido.");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }
        if (m_acessoViaUrl.getUsuario()==null) {
            m_respostaTexto.setMensagem("Usuário não encontrado em acesso");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }
        m_respostaTexto = s_cadastro.validarRedefinirSenha(senha,conf_senha,m_respostaTexto);
        if (!m_respostaTexto.isSucesso()) {
            return m_respostaTexto;
        }

        if (s_cadastro.validarSenha(m_acessoViaUrl.getUsuario(),senha)) {
            m_respostaTexto.setMensagem("Senha não pode ser a mesma que anterior");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }

        M_Usuario m_usuario = s_cadastro.redefinirSenha(m_acessoViaUrl.getUsuario(),senha);
        if (m_usuario==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Falha salvando senha");
            return m_respostaTexto;
        }

        m_respostaTexto.setMensagem("Alterou a senha de "+m_usuario.getNome()+" com sucesso!");
        return m_respostaTexto;
    }
}

package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.service.S_Cadastro;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class C_Cadastro {

    private final S_Cadastro s_cadastro;

    public C_Cadastro(S_Cadastro s_cadastro) {
        this.s_cadastro = s_cadastro;
    }

    /**
     * @return Tela de cadastro do cliente ou redireciona para {@link C_Inicio#redirecionar(HttpSession)} se já estiver logado
     */
    @GetMapping(path="/cadastro")
    public String getCadastroCliente(HttpSession session){
        if (session.getAttribute("usuario")!=null) {
            return "redirect:/";
        }
        return "visitante/cadastro";
    }

    /**
     * Cadastra um usuário se foi fornecido com os dados corretamente.
     * Redireciona para {@link C_Login#getLogin(HttpSession)}
     * @param nome Nome do cliente
     * @param senha Senha do cliente
     * @param conf_senha Confirmação da senha do cliente
     * @param email E-mail do cliente
     */
    @PostMapping(path="/cadastro")
    @ResponseBody
    public M_RespostaTexto cadastrarCliente(
            @RequestParam("nome") String nome,
            @RequestParam("senha") String senha,
            @RequestParam("conf_senha") String conf_senha,
            @RequestParam("email") String email) {
        M_RespostaTexto m_respostaTexto = s_cadastro.validarCadastroCliente(nome, senha, conf_senha, email);
        if (!m_respostaTexto.isSucesso()) {
            return m_respostaTexto;
        }

       if (s_cadastro.criarCadastroCliente(nome, senha, email)==null) {
           m_respostaTexto.setMensagem("Erro ao acessar banco de dados");
           m_respostaTexto.setSucesso(false);
           return m_respostaTexto;
       }
       m_respostaTexto.setMensagem("Conta cadastrada com sucesso.");
       return m_respostaTexto;
    }

    /**
     * Performa logout da conta do usuário o inibindo de ultilizar os recursos até fazer login novamente
     * @param session Sessão do usuário incluindo conta
     * @return Redirecionamento padrão sem cadastro {@link C_Inicio#redirecionar(HttpSession)}
     */
    @PostMapping(path="/logout")
    @ResponseBody
    public M_RespostaTexto Logout(HttpSession session){
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();
        if (session.getAttribute("usuario")==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Você não está cadastrado");
            return m_respostaTexto;
        }

        M_Usuario m_usuario = (M_Usuario) session.getAttribute("usuario");
        session.removeAttribute("usuario");
        session.removeAttribute("navbar");
        m_respostaTexto.setMensagem("Saiu da conta com sucesso, terá de se cadastrar novamente para acessar recursos de "+m_usuario.getCargo().toString()+".");
        m_respostaTexto.setSucesso(true);
        return m_respostaTexto;
    }
}

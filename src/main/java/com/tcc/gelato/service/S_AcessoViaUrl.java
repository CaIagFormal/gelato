package com.tcc.gelato.service;

import com.tcc.gelato.model.M_AcessoViaUrl;
import com.tcc.gelato.model.M_AcessoViaUrl.Funcionalidade;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.repository.R_AcessoViaUrl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;

/**
 * Regras de negócio e funcionalidade de {@link M_AcessoViaUrl}
 */
@Service
public class S_AcessoViaUrl {

    private final R_AcessoViaUrl r_acessoViaUrl;

    public S_AcessoViaUrl(R_AcessoViaUrl r_acessoViaUrl) {
        this.r_acessoViaUrl = r_acessoViaUrl;
    }

    /**
     * Gera número para um {@link com.tcc.gelato.model.M_AcessoViaUrl}
     * @return {@link String} do acesso
     */
    public String gerarNumeroDeTicket() {
        Random random = new Random();
        String ticket;
        String candidatos = "abcdefghijklmnopqrstuvwxyz0123456789";
        do {

            ticket = "";
            for (int i = 0; i < 10; i++) {
                ticket+=candidatos.charAt(random.nextInt(0,candidatos.length()));
            }
        } while (r_acessoViaUrl.getAcessoByTicket(ticket).isPresent());

        return ticket;
    }

    /**
     * Cria um acesso a uma funcionalidade via url que pode ser acessada em até um dia
     * @param funcionalidade Funcionalidade a ser acessada
     * @param m_usuario Usuário que usará a funcionalidade
     */
    public M_AcessoViaUrl criarAcesso(Funcionalidade funcionalidade, M_Usuario m_usuario) {
        M_AcessoViaUrl m_acessoViaUrl = new M_AcessoViaUrl();
        m_acessoViaUrl.setFuncionalidade(funcionalidade);
        m_acessoViaUrl.setTicket(gerarNumeroDeTicket());
        m_acessoViaUrl.setValidade(LocalDateTime.now().plusDays(1));
        m_acessoViaUrl.setUsuario(m_usuario);

        return r_acessoViaUrl.save(m_acessoViaUrl);
    }

    /**
     * Apaga um acesso
     * @param m_acessoViaUrl
     */
    public void deleteAcesso(M_AcessoViaUrl m_acessoViaUrl) {
        r_acessoViaUrl.delete(m_acessoViaUrl);
    }

    /**
     * Obtêm um acesso válido de um tipo determinado
     */
    public M_AcessoViaUrl getAcessoByTicketOfType(String ticket, Funcionalidade funcionalidade) {
        return r_acessoViaUrl.getAcessoByTicketOfTypeValido(ticket,funcionalidade.ordinal());
    }

    /**
     * Apaga todos os acessos que estão inválidos
     */
    public void apagarAcessosInvalidos() {
        r_acessoViaUrl.apagarAcessosInvalidos();
    }

    /**
     * Pega um acesso baseado em um usuário e tipo que ainda esteja válido
     */
    public M_AcessoViaUrl getAcessoByUsuarioAndType(M_Usuario m_usuario, Funcionalidade funcionalidade) {
        return r_acessoViaUrl.getAcessoByUsuarioAndTypeValido(m_usuario.getId(),funcionalidade.ordinal());
    }
}

package com.tcc.gelato.service;

import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.repository.R_Usuario;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aplicação de regras de negócio do Cadastro
 */
@Service
public class S_Cadastro {

    private final R_Usuario r_usuario;

    public S_Cadastro(R_Usuario r_usuario) {
        this.r_usuario = r_usuario;
    }

    /**
     * valida o cadastro do cliente
     * @param nome Nome do cliente
     * @param senha Senha do cliente
     * @param conf_senha Confirmação da senha do cliente
     * @param endereco Endereço em formato CEP do cliente
     * @param email E-mail do cliente
     * @param data_nasc Data de nascimento do cliente
     * @return se é valido
     */
    public boolean validarCadastroCliente(String nome, String senha, String conf_senha, String endereco, String email, String data_nasc) {
        boolean validade = !nome.trim().isBlank() &&
                !senha.trim().isBlank() &&
                !conf_senha.trim().isBlank() &&
                !endereco.trim().isBlank() &&
                !email.trim().isBlank() &&
                !data_nasc.trim().isBlank();

        if (!validade) return false;

        Pattern regex_cep = Pattern.compile("[0-9]{5}-[0-9]{3}",Pattern.CASE_INSENSITIVE);
        Matcher cep_valido = regex_cep.matcher(endereco);

        return LocalDate.now().minusYears(18).isAfter(LocalDate.parse(data_nasc)) &&
                senha.equals(conf_senha) &&
                cep_valido.find();
    }


    /**
     * Cadastra uma conta para um cliente
     * @param nome Nome do cliente
     * @param senha Senha do cliente
     * @param endereco Endereço em formato CEP do cliente
     * @param email E-mail do cliente
     * @param data_nasc Data de nascimento do cliente
     */
    public M_Usuario criarCadastroCliente(String nome, String senha, String endereco, String email, String data_nasc) {
        M_Usuario m_usuario = new M_Usuario();

        m_usuario.setNome(nome);
        m_usuario.setCargo(M_Usuario.Cargo.CLIENTE);
        m_usuario.setSenha(senha);
        m_usuario.setEndereco(endereco);
        m_usuario.setEmail(email);
        m_usuario.setDataNasc(Date.valueOf(data_nasc));

        try {
            return r_usuario.save(m_usuario);
        }
        catch (Exception ex) {
            return null;
        }
    }
}

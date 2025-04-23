package com.tcc.gelato.service;

import org.springframework.stereotype.Service;

/**
 * Aplicação de regras de negócio do Cadastro
 */
@Service
public class S_Cadastro {

    /**
     * valida o cadastro do cliente
     * @param nome Nome do cliente
     * @param senha Senha do cliente
     * @param confSenha Confirmação da senha do cliente
     * @param endereco Endereço do cliente
     * @param email E-mail do cliente
     * @return se é valido
     */
    public boolean validarCadastroCliente(String nome, String senha, String confSenha, String endereco, String email) {
        return  !nome.trim().isBlank() &&
                !senha.trim().isBlank() &&
                !confSenha.trim().isBlank() &&
                !endereco.trim().isBlank() &&
                !email.trim().isBlank() &&
                senha.equals(confSenha);
    }
}

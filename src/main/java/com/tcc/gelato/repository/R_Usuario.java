package com.tcc.gelato.repository;

import com.tcc.gelato.model.M_Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Repositório da tabela {@link M_Usuario}
 */
@Repository
public interface R_Usuario extends JpaRepository<M_Usuario, Long> {

    /**
     * Resgata um {@link com.tcc.gelato.model.M_Usuario} a partir dos parâmetros
     * @param nome Nome ou E-mail do usuário
     * @param senha Senha do usuário
     * @return {@link com.tcc.gelato.model.M_Usuario} correspondente
     */
    @Query(value = "select * from gelato.usuario where (nome=:NOME or email=:NOME) and senha=:SENHA limit 1",nativeQuery = true)
    M_Usuario getUsuarioByNomeOrEmailAndSenha(@Param("NOME") String nome, @Param("SENHA") String senha);
}

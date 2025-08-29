package com.tcc.gelato.repository;

import com.tcc.gelato.model.M_AcessoViaUrl;
import jakarta.transaction.Transactional;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositório para {@link com.tcc.gelato.model.M_AcessoViaUrl}
 */
public interface R_AcessoViaUrl extends JpaRepository<M_AcessoViaUrl,Long> {
    /**
     * Funcção que pega um acesso pelo ticket não importa se for válido ou não
     */
    @Query(value = "select * from gelato.acesso_via_url where ticket=:TICKET limit 1",nativeQuery = true)
    Optional<M_AcessoViaUrl> getAcessoByTicket(@Param("TICKET") String ticket);

    /**
     * Função que pega um acesso valido pelo ticket e funcionalidade
     */
    @Query(value = "select * from gelato.acesso_via_url where ticket=:TICKET and funcionalidade=:FUNC and validade>current_timestamp limit 1",nativeQuery = true)
    M_AcessoViaUrl getAcessoByTicketOfTypeValido(@Param("TICKET") String ticket,@Param("FUNC") int funcionalidade);

    /**
     * Pega um acesso baseado em um usuário e tipo que ainda esteja válido
     */
    @Query(value = "select * from gelato.acesso_via_url where fk_usuario=:USUARIO and funcionalidade=:FUNC and validade>current_timestamp limit 1",nativeQuery = true)
    M_AcessoViaUrl getAcessoByUsuarioAndTypeValido(@Param("USUARIO") Long usuario,@Param("FUNC") int funcionalidade);

    /**
     * Apaga todos os acessos via url que já expiraram
     */
    @Transactional
    @Modifying
    @Query(value = "delete from gelato.acesso_via_url where validade<current_timestamp",nativeQuery = true)
    void apagarAcessosInvalidos();
}

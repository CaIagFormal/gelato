package com.tcc.gelato.repository;

import com.tcc.gelato.model.M_AcessoViaUrl;
import org.springframework.data.jpa.repository.JpaRepository;
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
}

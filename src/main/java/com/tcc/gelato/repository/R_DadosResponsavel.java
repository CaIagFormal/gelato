package com.tcc.gelato.repository;

import com.tcc.gelato.model.M_DadosResponsavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface R_DadosResponsavel extends JpaRepository<M_DadosResponsavel,Long> {

    @Query(value = "select * from gelato.dados_responsavel where fk_usuario=:USUARIO order by id",nativeQuery = true)
    List<M_DadosResponsavel> getDadosResponsavelByUsuario(@Param("USUARIO") Long id_usuario);
}

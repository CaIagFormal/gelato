package com.tcc.gelato.repository;

import com.tcc.gelato.model.M_Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Repositório da tabela Usuário
 */
@Repository
public interface R_Usuario extends JpaRepository<M_Usuario, Long> {

}

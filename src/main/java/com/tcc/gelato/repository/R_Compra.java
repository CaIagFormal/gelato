package com.tcc.gelato.repository;

import com.tcc.gelato.model.M_Compra;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório da tabela {@link M_Compra}
 */
public interface R_Compra extends JpaRepository<M_Compra, Long> {

}

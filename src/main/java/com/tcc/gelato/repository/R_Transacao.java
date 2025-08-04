package com.tcc.gelato.repository;

import com.tcc.gelato.model.M_Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório ligado à {@link com.tcc.gelato.model.M_Transacao}
 */
public interface R_Transacao extends JpaRepository<M_Transacao,Long> {

}

package com.tcc.gelato.repository.produto;

import com.tcc.gelato.model.produto.M_Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório da tabela {@link M_Setor}
 */
@Repository
public interface R_Setor extends JpaRepository<M_Setor,Long> {

}

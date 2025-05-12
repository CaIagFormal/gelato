package com.tcc.gelato.repository.produto;
import com.tcc.gelato.model.produto.M_Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório da tabela {@link com.tcc.gelato.model.produto.M_Produto}
 */
@Repository
public interface R_Produto extends JpaRepository<M_Produto,Long> {

}

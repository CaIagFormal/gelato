package com.tcc.gelato.repository.produto;
import com.tcc.gelato.model.produto.M_Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório da tabela {@link com.tcc.gelato.model.produto.M_Produto}
 */
@Repository
public interface R_Produto extends JpaRepository<M_Produto,Long> {
    @Query(value = "select * from gelato.produto where disponivel = true",nativeQuery = true)
    public List<M_Produto> getProdutosDisponiveis();
}

package com.tcc.gelato.repository;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositório da tabela {@link M_Compra}
 */
public interface R_Compra extends JpaRepository<M_Compra, Long> {
    /**
     * Pega a quantidade de compras definidas como no carrinho de um usuário
     * @param id_usuario ID do {@link M_Usuario} em questão
     * @return Quantidade de {@link M_Compra}s no carrinho
     */
    @Query(value = "select count(*) from gelato.compra where id_usuario=:ID_USUARIO and status=0",nativeQuery = true)
    Integer getQtdComprasCarrinhoDeUsuario(@Param("ID_USUARIO") Long id_usuario);

    /**
     * Pega as compras definidas como no carrinho de um usuário em order cronológica do mais recente ao mais antigo
     * @param id_usuario ID do {@link M_Usuario} em questão
     * @return {@link M_Compra}s no carrinho
     */
    @Query(value = "select * from gelato.compra where id_usuario=:ID_USUARIO and status=0 order by horario desc",nativeQuery = true)
    List<M_Compra> getComprasCarrinhoDeUsuario(@Param("ID_USUARIO") Long id_usuario);
}

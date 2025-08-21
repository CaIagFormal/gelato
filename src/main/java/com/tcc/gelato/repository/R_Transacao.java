package com.tcc.gelato.repository;

import com.tcc.gelato.model.M_Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositório ligado à {@link M_Transacao}
 */
public interface R_Transacao extends JpaRepository<M_Transacao,Long> {

    String ao_vendedor_case = "(case when ao_vendedor then -1 else 1 end)";

    @Query(value = "select coalesce(sum(valor*"+ao_vendedor_case+"),'0.00'::decimal) as saldo from gelato.transacao where fk_cliente=:CLIENTE and valida",nativeQuery = true)
    BigDecimal getSaldoDeCliente(@Param("CLIENTE") Long id_cliente);

    @Query(value = "select * from gelato.transacao where fk_cliente=:CLIENTE",nativeQuery = true)
    List<M_Transacao> getTransacoesDeCliente(@Param("CLIENTE") Long id_cliente);
}

package com.tcc.gelato.model.servidor;

import com.tcc.gelato.model.M_Usuario;

import java.math.BigDecimal;

/**
 * Armazena todos os componentes da navbar do cargo {@link M_Usuario.Cargo#CLIENTE}
 */
public class M_NavbarCliente implements M_Navbar {

    public M_NavbarCliente() {

    }

    private Integer quantidade_compras;

    private BigDecimal saldo;

    public Integer getQuantidade_compras() {
        return quantidade_compras;
    }

    public void setQuantidade_compras(Integer quantidade_compras) {
        this.quantidade_compras = quantidade_compras;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}

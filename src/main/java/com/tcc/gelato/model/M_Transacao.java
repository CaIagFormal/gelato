package com.tcc.gelato.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Tabela que salva transações entre cliente e vendedor
 */
@Entity
@Table(name="transacao")
public class M_Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private BigDecimal valor;

    @JoinColumn(name="fk_cliente",nullable = false)
    @ManyToOne
    private M_Usuario cliente;

    @JoinColumn(name = "fk_vendedor",nullable = false)
    @ManyToOne
    private M_Usuario vendedor;

    @Column(nullable = false)
    private LocalDateTime horario_fornecido;
}

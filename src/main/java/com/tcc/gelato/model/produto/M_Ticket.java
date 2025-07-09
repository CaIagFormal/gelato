package com.tcc.gelato.model.produto;

import com.tcc.gelato.model.M_Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * O pedido em sí composto de um pacote de {@link com.tcc.gelato.model.M_Compra}s.
 */
@Entity
@Table(name="ticket")
public class M_Ticket {

    public enum StatusCompra {
        CARRINHO,
        ENCAMINHADO,
        PREPARADO,
        ENTREGUE,
        CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 8, nullable = false)
    private String ticket;

    @Column(nullable = false)
    private StatusCompra status;

    @JoinColumn(name="fk_usuario",nullable = false)
    @ManyToOne
    private M_Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime horario_fornecido;

    @Column()
    private LocalDateTime horario_encaminhado;

    @Column(nullable = false)
    private boolean pagamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public StatusCompra getStatus() {
        return status;
    }

    public void setStatus(StatusCompra status) {
        this.status = status;
    }

    public M_Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(M_Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getHorario_fornecido() {
        return horario_fornecido;
    }

    public void setHorario_fornecido(LocalDateTime horario_fornecido) {
        this.horario_fornecido = horario_fornecido;
    }

    public LocalDateTime getHorario_encaminhado() {
        return horario_encaminhado;
    }

    public void setHorario_encaminhado(LocalDateTime horario_encaminhado) {
        this.horario_encaminhado = horario_encaminhado;
    }

    public boolean isPagamento() {
        return pagamento;
    }

    public void setPagamento(boolean pagamento) {
        this.pagamento = pagamento;
    }
}

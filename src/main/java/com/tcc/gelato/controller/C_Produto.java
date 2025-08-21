package com.tcc.gelato.controller;

import com.tcc.gelato.model.M_Compra;
import com.tcc.gelato.model.M_Usuario;
import com.tcc.gelato.model.produto.M_Estoque;
import com.tcc.gelato.model.produto.M_Produto;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.servidor.M_Resposta;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class C_Produto {

    private final S_Produto s_produto;
    private final S_Cargo s_cargo;
    private final S_Ticket s_ticket;
    private final S_Compra s_compra;
    private final S_Estoque s_estoque;

    public C_Produto(S_Produto s_produto, S_Cargo s_cargo, S_Ticket s_ticket, S_Compra s_compra, S_Estoque s_estoque) {
        this.s_produto = s_produto;
        this.s_cargo = s_cargo;
        this.s_ticket = s_ticket;
        this.s_compra = s_compra;
        this.s_estoque = s_estoque;
    }

    /**
     *
     * @param id_produto ID do produto na URL
     * @param session Sessão usada para conferir {@link M_Usuario} válido
     * @return Página do produto solicitado
     */
    @GetMapping(path="/produto/{produto}")
    public String getProduto(@PathVariable("produto") Long id_produto, HttpSession session, Model model) {
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao();
        model.addAttribute("usuario",m_usuario);

        M_Produto m_produto = s_produto.getProdutoById(id_produto);

        model.addAttribute("produto",m_produto);
        model.addAttribute("estoque",s_estoque.getEstoqueForProduto(m_produto));

        if (s_cargo.validarCliente(m_usuario)) {
            M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);
            s_cargo.session_to_model_navbar(model,session);

            model.addAttribute("qtd_produto_carrinho",s_produto.getQtdDeProdutoEmTicket(m_produto,m_ticket));
        }
        return "produto";
    }

    /**
     * Cria uma {@link M_Compra} com
     * @param qtd Quantidade do produto
     * @param id_produto ID do produto
     * @param session Sessão para extrair o {@link M_Usuario}
     * @return Mensagem de sucesso ou falha para a página
     */
    @PostMapping(path="/adicionar_carrinho")
    @ResponseBody
    public M_RespostaTexto adicionarAoCarrinho(@RequestParam("qtd") String qtd, @RequestParam("id_produto") String id_produto, HttpSession session) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();
        M_Usuario m_usuario = s_cargo.extrairUsuarioDeSessao();

        M_Ticket m_ticket = s_ticket.conferirTicketDeUsuario(m_usuario);

        if (!s_ticket.validarTicketParaAlterarMonetario(m_ticket)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Seu pedido já foi pago e não pode ter seus conteúdos alterados.");
            return m_respostaTexto;
        }

        if (!s_compra.checkAdicionarAoCarrinhoValido(qtd,id_produto)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Sua compra é inválida.");
            return m_respostaTexto;
        }

        M_Produto m_produto = s_produto.getProdutoById(Long.parseLong(id_produto));
        if (m_produto==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Produto inválido.");
            return m_respostaTexto;
        }

        int qtd_int = Integer.parseInt(qtd);

        int qtd_no_carrinho = s_produto.getQtdDeProdutoEmTicket(m_produto,m_ticket);
        if (!s_estoque.conferirValidadeDeEstoque(m_produto,-(qtd_int+qtd_no_carrinho))) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Quantidade inválida.");
            return m_respostaTexto;
        }

        if (qtd_no_carrinho>0) {
            M_Compra m_compra = s_compra.getCompraComProdutoEmTicket(m_produto,m_ticket,m_produto.getPreco());
            if (m_compra==null) {
                m_respostaTexto.setSucesso(false);
                m_respostaTexto.setMensagem("Ocorreu um erro ao comunicar com o banco de dados.");
                return m_respostaTexto;
            }
            s_compra.adicionarQuantidade(m_compra,qtd_int);
            m_respostaTexto.setSucesso(true);
            m_respostaTexto.setMensagem(qtd+" "+m_produto.getMedida()+"(s) de "+m_produto.getNome()+" foram adicionados ao seu carrinho.");
            return m_respostaTexto;
        }

        M_Compra m_compra = s_compra.gerarCompraDoCarrinho(m_usuario,m_ticket,m_produto,qtd_int);

        if (m_compra==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Não foi possível registrar sua compra.");
            return m_respostaTexto;
        }

        // Atualizar quantidade de itens no carrinho
        s_cargo.navClienteSetQtdCompras(session,s_compra.getQtdComprasDeTicket(m_ticket));

        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem(qtd+" "+m_produto.getMedida()+"(s) de "+m_produto.getNome()+" foram adicionados ao seu carrinho.");
        return m_respostaTexto;
    }

    /**
     * Cria {@link M_Estoque} com
     * @param qtd Quantidade do produto
     * @param id_produto ID do produto
     * @return Mensagem de sucesso ou falha para a página
     */
    @PostMapping(path="/adicionar_estoque")
    @ResponseBody
    public M_RespostaTexto adicionarEstoque(@RequestParam("qtd") String qtd, @RequestParam("id_produto") String id_produto) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();

        if (!s_estoque.checkAdicionarEstoqueValido(qtd,id_produto)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Parâmetros inválidos.");
            return m_respostaTexto;
        }

        M_Produto m_produto = s_produto.getProdutoById(Long.parseLong(id_produto));
        if (m_produto==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Produto inválido.");
            return m_respostaTexto;
        }

        int qtd_int = Integer.parseInt(qtd);

        if (!s_estoque.conferirValidadeDeEstoque(m_produto,qtd_int)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Quantidade de estoque a ser adicionada é inválida.");
            return m_respostaTexto;
        }

        M_Estoque m_estoque = s_estoque.gerarEstoque(m_produto,qtd_int);

        if (m_estoque==null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Não foi possível alterar o estoque.");
            return m_respostaTexto;
        }

        Integer estoque = s_estoque.getEstoqueForProduto(m_produto);
        if (estoque==0) {
            m_produto.setDisponivel(false);
        }

        s_compra.corrigirComprasDeProdutoComQtdMaior(m_produto,estoque);

        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem(Math.abs(qtd_int) +" "+m_produto.getMedida()+"(s) de "+m_produto.getNome()+" foram "+((qtd_int>0)?"adicionados ao ":"removidos do ")+"estoque.");
        if (estoque==0) {
            m_respostaTexto.setMensagem(m_respostaTexto.getMensagem()+" (Produto não está mais disponível por estoque)");
        }
        return m_respostaTexto;
    }

    /**
     * Cria uma {@link M_Compra} com
     * @param id_compra ID da compra no carrinho para ser removida
     * @param session Sessão para extrair o {@link M_Usuario}
     * @return Mensagem de sucesso ou falha para a página
     */
    @PostMapping(path="/remover_carrinho")
    @ResponseBody
    public M_RespostaTexto removerDoCarrinho(@RequestParam("id_compra") String id_compra, HttpSession session) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();

        if (!s_compra.checkRemoverDoCarrinhoValido(id_compra)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Sua compra é inválida.");
            return m_respostaTexto;
        }

        M_Compra m_compra = s_compra.getCompraById(Long.parseLong(id_compra));
        if (m_compra == null) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Compra inválida.");
            return m_respostaTexto;
        }

        M_Ticket m_ticket = m_compra.getTicket();

        if (!s_ticket.validarTicketParaAlterarMonetario(m_ticket)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Seu pedido já foi pago e não pode ter seus conteúdos alterados.");
            return m_respostaTexto;
        }

        s_compra.removerCompra(m_compra);

        // Atualizar quantidade de itens no carrinho
        s_cargo.navClienteSetQtdCompras(session,s_compra.getQtdComprasDeTicket(m_ticket));

        m_respostaTexto.setSucesso(true);
        m_respostaTexto.setMensagem(m_compra.getQuantidade() + " " + m_compra.getProduto().getMedida() + "(s) de " + m_compra.getProduto().getNome() + " foram removidos do seu carrinho.");
        return m_respostaTexto;
    }
}

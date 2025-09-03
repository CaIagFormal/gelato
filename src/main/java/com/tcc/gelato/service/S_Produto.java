package com.tcc.gelato.service;

import com.tcc.gelato.controller.C_Produto;
import com.tcc.gelato.model.produto.M_Produto;
import com.tcc.gelato.model.produto.M_Ticket;
import com.tcc.gelato.model.servidor.M_RespostaTexto;
import com.tcc.gelato.repository.produto.R_Produto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Aplicação de regras de negócio de serviços relacionados a {@link M_Produto}
 */
@Service
public class S_Produto {

    private final R_Produto r_produto;

    public S_Produto(R_Produto r_produto) {
        this.r_produto = r_produto;
    }

    /**
     * @return Todos os {@link M_Produto}s disponíveis
     */
    public List<M_Produto> getProdutosDisponiveis() {
        return r_produto.getProdutosDisponiveis();
    }

    /**
     * @param id Id do produto
     * @return {@link M_Produto} ou Null se der exceção
     */
    public M_Produto getProdutoById(long id) {
        Optional<M_Produto> m_produto;
        try {
            m_produto = r_produto.findById(id);
        } catch (Exception e) {
            return null;
        }
        return m_produto.orElse(null);
    }

    /**
     * Retorna a soma das quantidade dentro das compras de um produto em um ticket
     * @param m_produto {@link M_Produto}
     * @param m_ticket {@link M_Ticket}
     * @return quantidade
     */
    public int getQtdDeProdutoEmTicket(M_Produto m_produto, M_Ticket m_ticket) {
        return r_produto.getQtdDeProdutoEmTicket(m_produto.getId(),m_ticket.getId());
    }

    /** Retorna todos os produtos organizados em disponibilidade
     *
     */
    public List<M_Produto> getProdutosVendedor() {
        return r_produto.getProdutosVendedor();
    }

    /**
     * Valida os parâmetros da função {@link C_Produto#criarProduto(String, String,String, String, String, String, String)}
     */
    public M_RespostaTexto validaParamCriarProduto(String nome, String descricao, String preco, String unidade, String url_icone, String estoque_minimo, String disponivel) {
        M_RespostaTexto m_respostaTexto = new M_RespostaTexto();
        if ((nome==null) || (descricao==null) || (preco==null) || (unidade==null) || (url_icone==null) || (estoque_minimo==null) || (disponivel==null)) {
            m_respostaTexto.setSucesso(false);
            m_respostaTexto.setMensagem("Um campo está nulo");
            return m_respostaTexto;
        }

        m_respostaTexto.setMensagem("");
        m_respostaTexto.setSucesso(true);

        boolean preco_invalido = false;
        boolean estoque_invalido = false;
        boolean disp_invalido = false;
        if (nome.isBlank()) {
            m_respostaTexto.appendMensagem("O nome está vazio<br>");
            m_respostaTexto.setSucesso(false);
        }
        if (preco.isBlank()) {
            m_respostaTexto.appendMensagem("O valor do preço está vazio<br>");
            m_respostaTexto.setSucesso(false);
            preco_invalido = true;
        }
        if (unidade.isBlank()) {
            m_respostaTexto.appendMensagem("A unidade do preço está vazia<br>");
            m_respostaTexto.setSucesso(false);
        }
        if (estoque_minimo.isBlank()) {
            m_respostaTexto.appendMensagem("O estoque mínimo está vazio<br>");
            m_respostaTexto.setSucesso(false);
            estoque_invalido = true;
        }
        if (disponivel.isBlank()) {
            m_respostaTexto.appendMensagem("Disponibilidade está vazia<br>");
            m_respostaTexto.setSucesso(false);
            disp_invalido = true;
        }

        if (!preco_invalido) {
            BigDecimal preco_val;
            try {
                preco_val = new BigDecimal(preco);
                if (preco_val.compareTo(BigDecimal.ZERO)<1) {
                    m_respostaTexto.appendMensagem("Preço está baixo demais");
                    m_respostaTexto.setSucesso(false);
                }
                if (preco_val.compareTo(new BigDecimal("100000000"))>-1) { // limite do postgres
                    m_respostaTexto.appendMensagem("Preço está alto demais");
                    m_respostaTexto.setSucesso(false);
                }
            } catch (Exception e) {
                m_respostaTexto.appendMensagem("Valor do preço inválido<br>");
                m_respostaTexto.setSucesso(false);
            }
        }

        if (!estoque_invalido) {
            int estoque_minimo_val;
            try {
                estoque_minimo_val = Integer.parseInt(estoque_minimo);
                if (estoque_minimo_val<0) {
                    m_respostaTexto.appendMensagem("Valor do estoque mínimo não pode ser negativo<br>");
                    m_respostaTexto.setSucesso(false);
                }
            } catch (Exception e) {
                m_respostaTexto.appendMensagem("valor do estoque mínimo inválido<br>");
                m_respostaTexto.setSucesso(false);
            }
        }

        if (!disp_invalido) {
            try {
                Boolean.parseBoolean(disponivel);
            } catch (Exception e) {
                m_respostaTexto.appendMensagem("Disponibilidade inválida<br>");
                m_respostaTexto.setSucesso(false);
            }
        }

        return m_respostaTexto;
    }

    /**
     * Cria um produto e os salva a partir dos parâmetros fornecidos
     */
    public M_Produto criarProduto(String nome,String descricao, BigDecimal preco, String unidade, String url_icone, int estoque_minimo, Boolean disponivel) {
        M_Produto m_produto = new M_Produto();
        m_produto.setNome(nome);
        m_produto.setDescricao(descricao);
        m_produto.setDisponivel(disponivel);
        m_produto.setPreco(preco);
        m_produto.setMedida(unidade);
        m_produto.setEstoque_minimo(estoque_minimo);
        m_produto.setUrl_icone(url_icone);

        return r_produto.save(m_produto);
    }

    /**
     * Valida os parâmetros da função {@link C_Produto#alterarProduto(String, String, String,String, String, String, String, String)}
     */
    public M_RespostaTexto validaParamAlterarProduto(String id, String nome, String descricao, String preco, String unidade, String urlIcone, String estoqueMinimo, String disponivel) {
        M_RespostaTexto m_respostaTexto = validaParamCriarProduto(nome,descricao,preco,unidade,urlIcone,estoqueMinimo,disponivel);
        if (!m_respostaTexto.isSucesso()) {
            return m_respostaTexto;
        }
        if (id==null||id.isBlank()) {
            m_respostaTexto.setMensagem("Detalhes do produto corrompidos, recarregue a página");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }

        long id_value;
        try {
            id_value = Long.parseLong(id);
            if (id_value<1) {
                m_respostaTexto.setMensagem("Detalhes do produto corrompidos, recarregue a página");
                m_respostaTexto.setSucesso(false);
                return m_respostaTexto;
            }
        } catch (Exception e) {
            m_respostaTexto.setMensagem("Detalhes do produto corrompidos, recarregue a página");
            m_respostaTexto.setSucesso(false);
            return m_respostaTexto;
        }
        return m_respostaTexto;
    }

    /**
     * Altera um produto a partir dos parâmetros fornecidos
     */
    public M_Produto alterarProduto(M_Produto m_produto, String nome,String descricao, BigDecimal preco, String unidade, String url_icone, int estoque_minimo, Boolean disponivel) {
        m_produto.setNome(nome);
        m_produto.setDescricao(descricao);
        m_produto.setDisponivel(disponivel);
        m_produto.setPreco(preco);
        m_produto.setMedida(unidade);
        m_produto.setEstoque_minimo(estoque_minimo);
        m_produto.setUrl_icone(url_icone);

        return r_produto.save(m_produto);
    }
}
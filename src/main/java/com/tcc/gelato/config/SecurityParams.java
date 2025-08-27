package com.tcc.gelato.config;

public class SecurityParams {
    public static String[] publico = new String[]{
            "/", "/catalogo", "/produto/**", // Get
            "/verificar_conta/**",
            "/js/funcionalidades.js",   // JS
            "/js/catalogo_cliente.js",
            "/js/observacao.js",
            "/css/index.css",           //CSS
            "/css/catalogo_cliente.css",
            "/css/produto_cliente.css",
            "/css/observacao.css",
            "/favicon.ico"};
    public static String[] visitante = new String[]{
            "/cadastrar", "/fazer_login", // Post
            "/cadastro", "/login",  // Get
            "/js/formularios.js", // JS
            "/js/cadastro_senha.js",
            "/css/index.css" //CSS
    };
    public static String[] vendedor = new String[]{
            "/inspecionar_saldo", "/inspecionar_transacoes", "/alterar_saldo", "/esvaziar_saldo", // Post
            "/adicionar_estoque","/obter_contato_cliente","/obter_pedido",
            "/gerir_saldo", // Get
            "/pedidos",
            "/js/gerir_saldo.js", // JS
            "/js/produto_vendedor.js",
            "/js/pedidos.js",
            "/css/gerir_saldo.css", // CSS
            "/css/catalogo_vendedor.css",
            "/css/pedidos.css"
    };
    public static String[] cliente = new String[]{
            "/adicionar_carrinho", "/remover_carrinho","/definir_horario_retirada_ticket", // Post
            "/encaminhar_pedido","/cancelar_pedido","/observacao_ticket",
            "/carrinho", // Get
            "/js/carrinho.js", // JS
            "/js/produto_cliente.js",
            "/css/carrinho_cliente.css", // CSS
    };
    public static String[] cadastrados = new String[]{
            "/js/cadastrados.js",   // JS
            "/fazer_logout"
    };
}

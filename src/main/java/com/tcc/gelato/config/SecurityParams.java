package com.tcc.gelato.config;

public class SecurityParams {
    public static String[] publico_post = new String[]{
    };
    public static String[] publico_get = new String[]{
            "/", "/catalogo", "/produto/**",
            "/verificar_conta/**",
            "/js/funcionalidades.js",   // JS
            "/js/catalogo_cliente.js",
            "/js/observacao.js",
            "/css/index.css",           //CSS
            "/css/catalogo_cliente.css",
            "/css/produto_cliente.css",
            "/css/observacao.css",
            "/favicon.ico"};

    public static String[] visitante_post = new String[]{
            "/cadastrar", "/fazer_login"
    };
    public static String[] visitante_get = new String[]{
            "/cadastro", "/login",
            "/js/formularios.js", // JS
            "/js/cadastro_senha.js",
            "/css/index.css" //CSS
    };
    public static String[] vendedor_post = new String[]{
            "/inspecionar_saldo", "/inspecionar_transacoes", "/alterar_saldo", "/esvaziar_saldo",
            "/adicionar_estoque","/obter_contato_cliente","/obter_pedido","/salvar_produto"
    };
    public static String[] vendedor_get = new String[]{
            "/gerir_saldo",
            "/pedidos",
            "/criar_produto",
            "/js/gerir_saldo.js", // JS
            "/js/produto_vendedor.js",
            "/js/criar_produto.js",
            "/js/pedidos.js",
            "/css/gerir_saldo.css", // CSS
            "/css/catalogo_vendedor.css",
            "/css/pedidos.css"
    };
    public static String[] cliente_post = new String[]{
            "/adicionar_carrinho", "/remover_carrinho", "/definir_horario_retirada_ticket",
            "/encaminhar_pedido", "/cancelar_pedido", "/observacao_ticket"
    };
    public static String[] cliente_get = new String[]{
            "/carrinho",
            "/js/carrinho.js", // JS
            "/js/produto_cliente.js",
            "/css/carrinho_cliente.css", // CSS
    };
    public static String[] cadastrados_post = new String[]{
            "/fazer_logout"
    };
    public static String[] cadastrados_get = new String[]{
            "/js/cadastrados.js"   // JS
    };
}

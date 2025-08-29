function btn_contato_cliente() {
    let nome = $(this).closest('.pedido').children('.nome_cliente').text();

    ajax("/obter_contato_cliente",{nome:nome});
}

$(".btn_contato_cliente").click(btn_contato_cliente)

function btn_obter_pedido() {
    let pedido = $(this).closest('.pedido')
    let id = $($(pedido).children('.id_ticket')).text();

    if ($(pedido).hasClass('carregado')) {
        return;
    }

    ajax("/obter_pedido",{id:id},inserir_pedido,false,pedido);
}

function inserir_pedido(resposta,pedido) {
    $(pedido).addClass('carregado')
    $($(pedido).find(".info_pedido")).html(resposta.mensagem);
}

$(".btn-obter-pedido").click(btn_obter_pedido)
function btn_contato_cliente() {
    let nome = (this).closest('.pedido').children('.nome_cliente');

    ajax("/obter_contato_cliente",{nome:nome});
}

$(".btn_contato_cliente").click(btn_contato_cliente)

function btn_obter_pedido() {
    let pedido = (this).closest('.pedido')
    let id = pedido.children('.id_ticket');

    if (pedido.hasClass('carregado')) {
        mostrar_erro('Já foi carregado este pedido!','Caso o pedido tenha sido atualizado recarregue a página.')
        return;
    }

    ajax("/obter_pedido",{id:id});
}

$(".btn_obter_pedido").click(btn_obter_pedido)
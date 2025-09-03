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

function conf_canc_pedido(btn) {
    let pedido = $(btn).closest('.pedido')
    let id = $($(pedido).children('.id_ticket')).text();


    ajax("/pedidos/cancelar",{id:id},cancelar_pedido,false,pedido);
}
function cancelar_pedido(resposta,pedido) {
    $(pedido).remove();
    let cancelados = $("#pedidos-cancelados").children("div")
    $(cancelados).prepend(resposta.mensagem);
    let btns = $('.btn-retrair:not(retrair-ativo)');
    $(btns).click(retrair);
    $(btns).addClass('retrair-ativo');
    $($(btns).find(".btn-obter-pedido")).click(btn_obter_pedido);
    $($(btns).find(".btn_canc_pedido")).click(btn_canc_pedido);
}

function btn_canc_pedido() {
    confirmar("Você deseja cancelar este pedido?", "Você não poderá voltar atrás!",conf_canc_pedido,this);
}
$(".btn-obter-pedido").click(btn_obter_pedido)

$(".btn_canc_pedido").click(btn_canc_pedido)
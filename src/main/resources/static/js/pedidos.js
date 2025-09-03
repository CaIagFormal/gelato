var btn_funcao = {
    ".btn-obter-pedido":f_obter_pedido,
    ".btn_canc_pedido":f_canc_pedido,
    ".btn_recebido":f_receber_pedido,
    ".btn_preparado":f_preparar_pedido,
    ".btn_entregue":f_entregar_pedido
}

function btn_contato_cliente() {
    let nome = $(this).closest('.pedido').children('.nome_cliente').text();

    ajax("/obter_contato_cliente",{nome:nome});
}

$(".btn_contato_cliente").click(btn_contato_cliente)

function f_obter_pedido() {
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

function conf_status_pedido(btn,alvo,url) {
    let pedido = $(btn).closest('.pedido')
    let id = $($(pedido).children('.id_ticket')).text();

    let alvo_div = $(alvo).children("div")
    let f_alvo = gerar_transfusao_de_pedido_para(pedido,alvo_div)
    ajax(url,{id:id},f_alvo,false);
}

function gerar_transfusao_de_pedido_para(param_antigo,param_local_alvo) {
    var antigo = param_antigo;
    var local_alvo = param_local_alvo
    return function(resposta) {

        $(local_alvo).prepend(resposta.mensagem);
        let btns = $(local_alvo).find('.btn-retrair:not(.retrair-ativo)');
        $(btns).click(retrair);
        $(btns).addClass('retrair-ativo');

        let pedido = $(btns[0]).closest(".pedido");


        $(Object.keys(btn_funcao)).each(function(i,e) {
            let p = $(pedido).find(e);
            if (p===null) {return true;} //continue
            $(p).click(btn_funcao[e]);
        });

        if ($(antigo).hasClass("carregado")) {
            let info = antigo.find(".info_pedido").html();
            pedido.find(".info_pedido").html(info);
            pedido.addClass("carregado");
        }

        $(antigo).remove();
    }
}

function conf_canc_pedido(btn) {
    conf_status_pedido(btn,"#pedidos-cancelados","/pedidos/cancelar")
}

function f_canc_pedido() {
    confirmar("Você deseja cancelar este pedido?", "Você não poderá voltar atrás!",conf_canc_pedido,this);
}

function conf_receber_pedido(btn) {
    conf_status_pedido(btn,"#pedidos-recebidos","/pedidos/avancar")
}

function f_receber_pedido() {
    confirmar("Você deseja receber este pedido?", "Faça isso se estiver pedindo ingredientes para este pedido ou já estiver preparando.",conf_receber_pedido,this);
}

function conf_preparar_pedido(btn) {
    conf_status_pedido(btn,"#pedidos-preparados","/pedidos/avancar")
}

function f_preparar_pedido() {
    confirmar("Você terminou de preparar este pedido?", "Faça isso se acabou de concluir a preparação do pedido.",conf_preparar_pedido,this);
}

function conf_entregar_pedido(btn) {
    conf_status_pedido(btn,"#pedidos-entregues","/pedidos/avancar")
}

function f_entregar_pedido() {
    confirmar("Você entregou este pedido?", "Faça isso apenas se acabou de entregar o pedido.",conf_entregar_pedido,this);
}


$(Object.keys(btn_funcao)).each(function(i,e) {
    $(e).click(btn_funcao[e]);
});
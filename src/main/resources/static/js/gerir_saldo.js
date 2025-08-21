function saldo_func(erro_txt,dir=1) {
    let qtd = $("#quantidade").val();
    let cliente = $("#cliente").val();

    let erro = ""
    erro = conf_qtd(qtd,erro);

    if (cliente.trim()=="") {
        erro += "O cliente está vazio, por favor preencha o campo.<br>";
    }

    if (erro!="") {
        mostrar_erro(erro_txt,erro);
        return;
    }

    ajax("/alterar_saldo",{cliente:cliente,qtd:qtd*dir});

}

function f_adicionar_saldo_conf() {
    saldo_func("Algo ocorreu adicionando saldo...")
}

function f_remover_saldo_conf() {
    saldo_func("Algo ocorreu removendo saldo...",dir=-1)
}

function f_esvaziar_saldo_conf() {
    let cliente = $("#cliente").val();
    let erro = ""

    if (cliente.trim()=="") {
        erro += "O cliente está vazio, por favor preencha o campo.<br>";
    }

    if (erro!="") {
        mostrar_erro("Algo ocorreu esvaziando saldo...",erro);
        return;
    }

    ajax("/esvaziar_saldo",{cliente:cliente});
}

function f_adicionar_saldo() {
    let qtd = $("#quantidade").val();
    let cliente = $("#cliente").val();
    confirmar("Você deseja adicionar "+quantidade+"R$ na conta de "+cliente+"?", "Todas as transações são registradas.",f_adicionar_saldo_conf)
}

function f_remover_saldo() {
    let qtd = $("#quantidade").val();
    let cliente = $("#cliente").val();
    confirmar("Você deseja remover "+quantidade+"R$ da conta de "+cliente+"?", "Todas as transações são registradas.",f_remover_saldo_conf)
}

function f_esvaziar_saldo() {
    let cliente = $("#cliente").val();
    confirmar("Você deseja esvaziar o saldo de "+cliente+"?", "Todas as transações são registradas.",f_esvaziar_saldo_conf)
}

$("#btn_add_saldo").click(f_adicionar_saldo);
$("#btn_rem_saldo").click(f_remover_saldo);
$("#btn_vazio_saldo").click(f_esvaziar_saldo);

function insp_func(url,f_insp) {
    let cliente = $("#cliente").val();
    let erro = ""

    if (cliente.trim()=="") {
        erro += "O cliente está vazio, por favor preencha o campo.<br>";
    }

    if (erro!="") {
        mostrar_erro("Algo ocorreu inspecionado o saldo saldo...",erro);
        return;
    }

    ajax(url,{cliente:cliente},f_insp,false);
}

function f_inspecionar_saldo() {
    insp_func("/inspecionar_saldo",pos_insp_saldo)
}

function f_inspecionar_transacoes() {
    insp_func("/inspecionar_transacoes",pos_insp_trans)
}

var consulta_template = $("#template-consulta")
var consulta_template_container = $("#template-consulta-container")

function pos_insp_saldo(resposta) {
    if (!resposta.sucesso) { return; }
    let cliente = $("#cliente").val();
    let consultas = $("#consultas")

    let con_div = consulta_template.clone();
    con_div.removeAttr("id");
    con_div.removeClass("d-none")

    con_div.append("<p class='col'> <b>Cliente:</b> "+cliente+"</p>")
    con_div.append("<p class='col'> <b>Saldo:</b> "+resposta.mensagem+"R$</p>")
    con_div.append("<p class='col'> <b>Horário:</b> "+data_to_string(new Date())+"</p>")

    consultas.prepend(con_div);
    return;
}

function pos_insp_trans(resposta) {
    if (!resposta.sucesso) { return; }
    let cliente = $("#cliente").val();
    let consultas = $("#consultas")

    let big_con_div = $(consulta_template_container).clone()
    big_con_div.removeAttr("id");
    big_con_div.removeClass("d-none");

    $(resposta.mensagem).each((i,transacao) => {
        let con_div = consulta_template.clone();
        con_div.removeAttr("id");
        con_div.removeClass("d-none");
        let sinal = (transacao[1][3] == 'v')?"neg":"pos"; // 'Ao [v]endedor' / 'Ao [c]liente'
        con_div.append("<p class='col "+sinal+"'> <b>Valor:</b> "+transacao[0]+"R$</p>")
        con_div.append("<p class='col'> <b>Ao Vendedor:</b> "+transacao[1]+"</p>")
        con_div.append("<p class='col'> <b>Cliente:</b> "+transacao[2]+"</p>")
        con_div.append("<p class='col'> <b>Vendedor:</b> "+transacao[3]+"</p>")
        con_div.append("<p class='col'> <b>Horário:</b> "+transacao[4]+"</p>")

        big_con_div.prepend(con_div);
    })

    $(big_con_div).prepend("<h5>CONSULTOU TRANSAÇÕES DE CLIENTE "+cliente+" ÀS "+data_to_string(new Date())+"</h5>");
    consultas.prepend(big_con_div)
    return;
}
$("#btn_insp_saldo").click(f_inspecionar_saldo);
$("#btn_insp_trans").click(f_inspecionar_transacoes);
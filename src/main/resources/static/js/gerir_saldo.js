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

function f_adicionar_saldo() {
    saldo_func("Algo ocorreu adicionando saldo...")
}

function f_remover_saldo() {
    saldo_func("Algo ocorreu removendo saldo...",dir=-1)
}

function f_esvaziar_saldo() {
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

function pos_insp_saldo(resposta) {
    if (!resposta.sucesso) { return; }
    let cliente = $("#cliente").val();
    let consultas = $("#consultas")

    let con_div = consulta_template.clone();
    con_div.removeAttr("id");
    con_div.removeClass("d-none")

    con_div.append("<p class='col'> <b>Cliente:</b> "+cliente+"</p>")
    con_div.append("<p class='col'> <b>Saldo:</b> "+resposta.mensagem+"R$</p>")
    con_div.append("<p class='col'> <b>Horário:</b> "+(new Date()).toString()+"</p>")

    consultas.prepend(con_div);
    return;
}

function pos_insp_trans(resposta) {
    if (!resposta.sucesso) { return; }
    let cliente = $("#cliente").val();
    let consultas = $("#consultas")

    $(resposta.mensagem.split("||")).each((i,str) => {
        transacao = str.split(";");
        let con_div = consulta_template.clone();
        con_div.removeAttr("id");
        con_div.removeClass("d-none")

        con_div.append("<p class='col'> <b>Valor:</b> "+transacao[0]+"R$</p>")
        con_div.append("<p class='col'> <b>Ao Vendedor:</b> "+transacao[1]+"</p>")
        con_div.append("<p class='col'> <b>Cliente:</b> "+transacao[2]+"</p>")
        con_div.append("<p class='col'> <b>Vendedor:</b> "+transacao[3]+"</p>")
        con_div.append("<p class='col'> <b>Horário:</b> "+transacao[4]+"</p>")

        consultas.prepend(con_div);
    })

    return;
}
$("#btn_insp_saldo").click(f_inspecionar_saldo);
$("#btn_insp_trans").click(f_inspecionar_transacoes);
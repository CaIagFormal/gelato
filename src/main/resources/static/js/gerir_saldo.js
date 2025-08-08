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
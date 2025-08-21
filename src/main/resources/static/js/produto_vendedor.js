function conf_adicionar_estoque() {
    let qtd = $("#qtd_e").val();
    let id_produto = $("#id_produto").text();

    let erro = ""
    erro = conf_qtd(qtd,erro,neg=false);

    if (erro!="") {
        mostrar_erro("Algo ocorreu reestocando...",erro);
        return;
    }

    ajax("/adicionar_estoque",{qtd:qtd,id_produto:id_produto},recarregar_no_sucesso);
}

function adicionar_estoque() {
    confirmar("Você deseja adicionar estoque?","Todas as operações de estoque são registradas.",conf_adicionar_estoque);
}

$("#btn_estoque").click(adicionar_estoque)

function conf_adicionar_ao_carrinho() {
    let qtd = $("#qtd").val();
    let id_produto = $("#id_produto").text();

    let erro = "";
    erro = conf_qtd(qtd,erro);

    if (erro!="") {
        mostrar_erro("Sua compra falhou...",erro);
        return;
    }

    ajax("/adicionar_carrinho",{qtd:qtd,id_produto:id_produto},recarregar_no_sucesso);
}

function adicionar_ao_carrinho() {
    confirmar("Você deseja comprar este item?", "Você poderá remover do carrinho na tela do mesmo depois.",conf_adicionar_ao_carrinho)
}

$("#btn_comprar").click(adicionar_ao_carrinho);
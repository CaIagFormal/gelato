function conf_remover_item_do_carrinho(btn) {
    let id = $(btn).parent().parent().parent().parent().children("p.id_compra").text()

    erro = ""
    if (parseInt(id)===NaN) {
        erro += "Erro de parâmetros locais<br>";
    }
    else if (parseInt(id)<0) {
        erro += "Erro do produto<br>";
    }

    if (erro != "") {
        mostrar_erro("Algo de errado ocorreu!",erro);
        return;
    }

    ajax("/remover_carrinho",{id_compra:id})
}

function remover_item_do_carrinho() {
    swalWithBootstrapButtons.fire({
       title: "Você deseja comprar este item?",
       text: "Você poderá remover do carrinho na tela do mesmo depois.",
       icon: "warning",
       showCancelButton: true,
       confirmButtonText: "Sim.",
       cancelButtonText: "Não.",
       reverseButtons: true
     }).then((result) => {
       if (result.isConfirmed) {
            conf_remover_item_do_carrinho(this)
       }
     });
}

$(".remover-item").click(remover_item_do_carrinho)
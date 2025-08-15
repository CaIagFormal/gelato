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

    ajax("/remover_carrinho",{id_compra:id},recarregar_no_sucesso)
}

function remover_item_do_carrinho() {
    swalWithBootstrapButtons.fire({
       title: "Você deseja remover este item?",
       text: "Você poderá adicionar ao carrinho pela tela do produto do mesmo depois, a não ser que o pedido já seja encomendado.",
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
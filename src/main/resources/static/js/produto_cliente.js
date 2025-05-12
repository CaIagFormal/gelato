function mostrar_erro(titulo,erro) {
    Swal.fire({
      title: titulo,
      html: erro,
      icon: "error"
    });
}

function adicionar_ao_carrinho() {
    let qtd = $("#qtd").val();
    let id_produto = $("#id_produto").val();

    let erro = ""
    if (parseInt(qtd)===NaN) {
        erro += "A quantidade inserida é invalida.<br>"
    }

    if (parseInt(qtd)<=0) {
        erro += "A quantidade inserida é negativa."
    }

    if (erro!="") {
        mostrar_erro("Sua compra falhou...",erro);
        return;
    }

    $.ajax({
        type: "POST",
        url: "adicionar_carrinho/"+id_produto,
        data: {qtd:qtd},
        success: function(retorno) {
            alert(retorno);
        }
    })
}

$("#btn_comprar").click(adicionar_ao_carrinho)
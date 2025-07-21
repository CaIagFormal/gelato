function mostrar_erro(titulo,erro) {
    Swal.fire({
      title: titulo,
      html: erro,
      icon: "error"
    });
}

function adicionar_estoque() {
    let qtd = $("#qtd_e").val();
    let id_produto = $("#id_produto").text();

    let erro = ""
    if (parseInt(qtd)===NaN) {
        erro += "A quantidade inserida é invalida.<br>"
    }

    if (parseInt(qtd)==0) {
            erro += "A quantidade inserida é zero."
    }

    if (erro!="") {
        mostrar_erro("Algo ocorreu reestocando...",erro);
        return;
    }

    $.ajax({
        type: "POST",
        url: "/adicionar_estoque",
        data: {qtd:qtd,id_produto:id_produto},
        success: function(retorno) {
            if (retorno.sucesso) {
                Swal.fire({
                  title: "Êxito!",
                  html: retorno.mensagem,
                  icon: "success"
                });
            } else {
                Swal.fire({
                  title: "Algo de errado aconteceu",
                  html: retorno.mensagem,
                  icon: "error"
                });
            }
        }
    })
}

$("#btn_estoque").click(adicionar_estoque)

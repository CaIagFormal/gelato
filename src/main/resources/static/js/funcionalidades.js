function mostrar_erro(titulo,erro) {
    Swal.fire({
      title: titulo,
      html: erro,
      icon: "error"
    });
}

function conf_qtd(qtd,erro,neg=true) {
    if (qtd.trim()=="") {
        erro += "A quantidade inserida está vazia, por favor preencha o campo.<br>"
        return erro
    }

    if (parseInt(qtd)===NaN) {
        erro += "A quantidade inserida é invalida.<br>"
        return erro
    }

    if (parseInt(qtd)<0&&neg) {
        erro += "A quantidade inserida é negativa.<br>"
        return erro
    }

    if (parseInt(qtd)==0) {
        erro += "A quantidade inserida é zero.<br>"
        return erro
    }

    return erro
}

function ajax(v_url,v_data) {
    $.ajax({
            type: "POST",
            url: v_url,
            data: v_data,
            success: function(retorno) {
                if (retorno.sucesso) {
                    Swal.fire({
                      title: "Êxito!",
                      html: retorno.mensagem,
                      icon: "success"
                    });
                } else {
                    mostrar_erro("Algo de errado ocorreu.",retorno.mensagem)
                }
            },
            error: function(retorno) {
                mostrar_erro("Erro no servidor","Cód. "+retorno.status+" ("+retorno.responseJSON.error+")")
            }
        })
}

const swalWithBootstrapButtons = Swal.mixin({
  customClass: {
    confirmButton: "btn btn-success m-2",
    cancelButton: "btn btn-danger m-2"
  },
  buttonsStyling: true
});
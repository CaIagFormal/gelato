function mostrar_erro(titulo,erro,f_then=null) {
    Swal.fire({
      title: titulo,
      html: erro,
      icon: "error"
    }).then(() => {
      if (f_then===null) {
          return;
      } else {
          f_then({mensagem:erro,sucesso:false});
      }
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

function ajax(v_url,v_data,f_then=null,exito=true) {
    $.ajax({
            type: "POST",
            url: v_url,
            data: v_data,
            success: function(retorno) {
                if (retorno.sucesso) {
                    if (!exito) {
                        if (f_then===null) {return;}
                        f_then(retorno);
                        return;
                    }
                    Swal.fire({
                      title: "Êxito!",
                      html: retorno.mensagem,
                      icon: "success"
                    }).then(() => {
                        if (f_then===null) {
                            return;
                        } else {
                            f_then(retorno);
                        }
                    });

                } else {
                    mostrar_erro("Algo de errado ocorreu.",retorno.mensagem,f_then);
                }
            },
            error: function(retorno) {
                mostrar_erro("Erro no servidor","Cód. "+retorno.status,f_then);
            }
        });
}

function recarregar_no_sucesso(resposta) {
    if (resposta.sucesso) {
        location.reload()
    }
}

const swalWithBootstrapButtons = Swal.mixin({
  customClass: {
    confirmButton: "btn btn-success m-2",
    cancelButton: "btn btn-danger m-2"
  },
  buttonsStyling: true
});

function data_to_string(data) {
    return data.getDate()+"/"+(data.getMonth()+1)+"/"+data.getFullYear()+" "+data.getHours()+":"+data.getMinutes()+":"+data.getSeconds();
}
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

function ajax(v_url,v_data,f_then=null,exito=true,context=null) {
    $.ajax({
            type: "POST",
            url: v_url,
            data: v_data,
            success: function(retorno) {
                if (retorno.sucesso) {
                    if (!exito) {
                        if (f_then===null) {return;}
                        if (context===null) {f_then(retorno);return;}
                        f_then(retorno,context);
                        return;
                    }
                    Swal.fire({
                      title: "Êxito!",
                      html: retorno.mensagem,
                      icon: "success"
                    }).then(() => {
                        if (f_then===null) {
                            return;
                        }
                        if (context===null) {
                            f_then(retorno);
                            return;
                        }
                        f_then(retorno,context)
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
    return pad(data.getDate())+"/"+pad(data.getMonth()+1)+"/"+data.getFullYear()+" "+pad(data.getHours())+":"+pad(data.getMinutes())+":"+pad(data.getSeconds());
}

function pad(val) {
    return String(val).padStart(2, '0');
}

function confirmar(title,text,f_then,context=this) {
    swalWithBootstrapButtons.fire({
       title: title,
       text: text,
       icon: "warning",
       showCancelButton: true,
       confirmButtonText: "Sim.",
       cancelButtonText: "Não.",
       reverseButtons: true
     }).then((result) => {
       if (result.isConfirmed) {
            f_then(context)
       }
     });
}

//Funcionalidade de retrair
function retrair() {
    let retraivel;
    if ($(this).hasClass('r-orphan')) {
        retraivel = $(this).next(".retraivel")
    } else {
        let parent = $(this).closest('div.retrair-parent');
        retraivel = $(parent).children(".retraivel");
    }
    if ($(retraivel).hasClass("d-none")) {
        retraivel.removeClass("d-none");
        $(this).removeClass("retraido");
    } else {
        retraivel.addClass("d-none");
        $(this).addClass("retraido");
    }
}
function preparar_retraiveis() {
    let btns = $('.btn-retrair:not(retrair-ativo)');
    $(btns).click(retrair)
    $(btns).addClass('retrair-ativo')
}
$('document').ready(preparar_retraiveis);
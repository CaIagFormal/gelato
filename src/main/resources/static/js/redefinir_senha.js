function redefinir_senha() {
    let senha = $("#senha").val();
    let conf_senha = $("#conf_senha").val();
    let acesso = window.location.pathname.split("/")[2];

    let erro = ""
    if (senha.trim()=="") {
        erro += "A senha está vazia, por favor preencha o campo.<br>";
    }
    if (conf_senha.trim()=="") {
        erro += "A confirmação está vazio, por favor preencha o campo.<br>";
    }

    if (senha.trim()!="" && senha!=conf_senha) {
        erro += "A senha não foi confirmada corretamente.<br>";
    }

    if (erro != "") {
        mostrar_erro("Cadastro inválido",erro);
        return
    }

    ajax("/redefinir_senha/"+acesso,{senha:senha,conf_senha:conf_senha});
}

$("#btn-enviar").click(redefinir_senha)
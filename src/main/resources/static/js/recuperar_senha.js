function recuperar_senha() {
    let nome = $("#nome").val();

    let erro = ""
    if (nome.trim()=="") {
        erro += "O nome/email está vazio, por favor preencha o campo.<br>";
    }

    if (erro != "") {
        mostrar_erro("Cadastro inválido",erro);
        return
    }

    ajax("/recuperar_senha",{nome:nome});
}

$("#btn-enviar").click(recuperar_senha);
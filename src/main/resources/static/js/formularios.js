function login() {
    let nome = $("#nome").val()
    let senha = $("#senha").val();

    let erro = ""
    if (nome.trim()=="") {
        erro += "O nome está vazio, por favor preencha o campo.<br>";
    }
    if (senha.trim()=="") {
        erro += "A senha está vazia, por favor preencha o campo.";
    }

    if (erro != "") {
        mostrar_erro("Cadastro inválido",erro);
        return
    }

    ajax("/fazer_login",{nome:nome,senha:senha},apos_login);
}

function cadastro() {
    let nome = $("#nome").val()
    let senha = $("#senha").val()
    let conf_senha = $("#conf_senha").val()
    let email = $("#email").val()

    let erro = ""
    if (nome.trim()=="") {
        erro += "O nome está vazio, por favor preencha o campo.<br>";
    }
    if (senha.trim()=="") {
        erro += "A senha está vazia, por favor preencha o campo.<br>";
    }
    if (conf_senha.trim()=="") {
        erro += "A senha não foi confirmada, preencha o campo.<br>";
    }
    if (email.trim()=="") {
        erro += "O e-mail está vazio, por favor preencha o campo.<br>";
    }

    if (senha.trim()!="" && senha!=conf_senha) {
        erro += "A senha não foi confirmada corretamente.<br>";
    }

    if (erro != "") {
        mostrar_erro("Cadastro inválido",erro);
        return
    }

    ajax("/cadastrar",{nome:nome,senha:senha,conf_senha:conf_senha,email:email},apos_cadastro);
}

function apos_cadastro(resposta) {
    if (resposta.sucesso) {
        window.location = "/login";
    }
}

function apos_login(resposta) {
    if (resposta.sucesso) {
        window.location = "/catalogo";
    }
}

$("#f-login").click(login)
$("#f-cadastro").click(cadastro)
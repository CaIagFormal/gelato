function mostrar_erro(titulo,erro) {
    Swal.fire({
      title: titulo,
      html: erro,
      icon: "error"
    });
}

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

    $("form").trigger("submit");
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

    $("form").trigger("submit");
}

$("#f-login").click(login)
$("#f-cadastro").click(cadastro)
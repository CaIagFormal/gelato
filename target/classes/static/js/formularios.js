function login() {
    let nome = $("#nome").val()
    let senha = $("#senha").val()

    let erro = ""
    if (nome.trim()=="") {
        erro += "O nome está vazio, por favor preencha o campo.\n";
    }
    if (senha.trim()=="") {
        erro += "A senha está vazia, por favor preencha o campo.";
    }

    if (erro != "") {
        Swal.fire({
          title: "Cadastro inválido",
          text: erro,
          icon: "error"
        });
        return
    }

    $("form").trigger("submit");
}

function cadastro() {
    let nome = $("#nome").val()
    let senha = $("#senha").val()
    let conf_senha = $("#conf_senha").val()
    let endereco = $("#endereco").val()
    let email = $("#email").val()

    let erro = ""
    if (nome.trim()=="") {
        erro += "O nome está vazio, por favor preencha o campo.\n";
    }
    if (senha.trim()=="") {
        erro += "A senha está vazia, por favor preencha o campo.\n";
    }
    if (conf_senha.trim()=="") {
        erro += "A senha não foi confirmada, por favor preencha o campo.\n";
    }
    if (endereco.trim()=="") {
        erro += "O endereço está vazio, por favor preencha o campo.\n";
    }
    if (email.trim()=="") {
        erro += "O e-mail está vazio, por favor preencha o campo.\n";
    }

    if (senha.trim()!="" && senha!=conf_senha) {
        erro += "A senha não foi confirmada corretamente.";
    }

    if (erro != "") {
        Swal.fire({
          title: "Cadastro inválido",
          text: erro,
          icon: "error"
        });
        return
    }

    $("form").trigger("submit");
}

$("#f-login").click(login)
$("#f-cadastro").click(cadastro)
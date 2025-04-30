var ms_maior_de_idade = 1000*60*60*24*365*18
var re_cep = new RegExp("[0-9]{5}-[0-9]{3}")

function login() {
    let nome = $("#nome").val()
    let senha = $("#senha").val()

    let erro = ""
    if (nome.trim()=="") {
        erro += "O nome está vazio, por favor preencha o campo.<br>";
    }
    if (senha.trim()=="") {
        erro += "A senha está vazia, por favor preencha o campo.";
    }

    if (erro != "") {
        Swal.fire({
          title: "Cadastro inválido",
          html: erro,
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
    let data_nasc = $("#data_nasc").val()

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
    if (endereco.trim()=="") {
        erro += "O endereço está vazio, por favor preencha o campo.<br>";
    }
    if (email.trim()=="") {
        erro += "O e-mail está vazio, por favor preencha o campo.<br>";
    }
    if (data_nasc.trim()=="") {
        data_nasc += "A data de nascimento está vazio, por favor preencha o campo.<br>";
    }
    if (Date.now()-Date.parse(data_nasc)<ms_maior_de_idade) {
        erro += "Você prescisa ser maior de idade para poder ultilizar nossos serviços.<br>"
    }

    if (senha.trim()!="" && senha!=conf_senha) {
        erro += "A senha não foi confirmada corretamente.<br>";
    }
    if (!(re_cep.test(endereco))) {
        erro += "O endereço não foi formatado corretamente."
    }

    if (erro != "") {
        Swal.fire({
          title: "Cadastro inválido",
          html: erro,
          icon: "error"
        });
        return
    }

    $("form").trigger("submit");
}

$("#f-login").click(login)
$("#f-cadastro").click(cadastro)
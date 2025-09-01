function conf_criar_produto() {
    let nome = $("#nome").val()
    let descricao = $("#obs-write").val().trim()
    let preco = $("#preco").val()
    let unidade = $("#unidade").val()
    let url_icone = $("#url_icone").val()
    let estoque_minimo = $("#estoque_minimo").val()
    let disponivel = $('#disponivel').is(":checked")

    let erro = ""
    if (nome.trim()=="") {
        erro += "O nome está vazio, por favor preencha o campo.<br>";
    }
    if (preco.trim()=="") {
        erro += "O valor do preço está vazio, por favor preencha o campo.<br>";
    }
    if (unidade.trim()=="") {
        erro += "A unidade de medida do preço está vazia, por favor preencha o campo.<br>";
    }
    if (estoque_minimo.trim()=="") {
        erro += "O estoque mínimo está vazio, por favor preencha o campo.<br>";
    }

    if (erro != "") {
        mostrar_erro("Cadastro inválido",erro);
        return
    }

    ajax("/criar_produto",{nome:nome,preco:preco,unidade:unidade,estoque_minimo:estoque_minimo,disponivel:disponivel});
}

function criar_produto() {
    confirmar("Você deseja criar este produto?", "Você poderá alterá-lo, mas não apagá-lo depois.",conf_criar_produto)
}

$("#btn-criar-produto").click(criar_produto);
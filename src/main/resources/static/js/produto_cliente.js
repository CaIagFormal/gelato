function conf_adicionar_ao_carrinho() {
    let qtd = $("#qtd").val();
    let id_produto = $("#id_produto").text();

    let erro = "";
    erro = conf_qtd(qtd,erro);

    if (erro!="") {
        mostrar_erro("Sua compra falhou...",erro);
        return;
    }

    ajax("/adicionar_carrinho",{qtd:qtd,id_produto:id_produto});
}

function adicionar_ao_carrinho() {
    swalWithBootstrapButtons.fire({
       title: "Você deseja comprar este item?",
       text: "Você poderá remover do carrinho na tela do mesmo depois.",
       icon: "warning",
       showCancelButton: true,
       confirmButtonText: "Sim.",
       cancelButtonText: "Não.",
       reverseButtons: true
     }).then((result) => {
       if (result.isConfirmed) {
            conf_adicionar_ao_carrinho()
       }
     });
}

$("#btn_comprar").click(adicionar_ao_carrinho);
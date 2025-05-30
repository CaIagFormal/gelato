function abrir_produto() {
    let id_produto = $($(this).find(".id_produto")[0]).text();
    $("form").attr("action","produto/"+id_produto);
    $("form").trigger("submit");
}

function abrir_setor() {
    if ($(this).hasClass("aberto")) {
        $(this).next(".setor").addClass("d-none");
        $(this).removeClass("aberto");
    } else {
        $(this).next(".d-none.setor").removeClass("d-none");
        $(this).addClass("aberto");
    }
}

$(".btn_produto").click(abrir_produto);

$(".btn_setor").click(abrir_setor);
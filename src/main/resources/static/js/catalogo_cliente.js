function abrir_produto() {
    let id_produto = $($(this).find(".id_produto")[0]).text();
    $("form").attr("action","produto/"+id_produto);
    $("form").trigger("submit");
}

$(".btn_produto").click(abrir_produto);
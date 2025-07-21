function remover_item_do_carrinho() {
    console.log($(this).parent())
}

$(".remover-item").click(remover_item_do_carrinho)
function conf_remover_item_do_carrinho(btn) {
    let id = $(btn).parent().parent().parent().parent().children("p.id_compra").text()

    erro = ""
    if (parseInt(id)===NaN) {
        erro += "Erro de parâmetros locais<br>";
    }
    else if (parseInt(id)<0) {
        erro += "Erro do produto<br>";
    }

    if (erro != "") {
        mostrar_erro("Algo de errado ocorreu!",erro);
        return;
    }

    ajax("/remover_carrinho",{id_compra:id},recarregar_no_sucesso)
}

function remover_item_do_carrinho() {
    confirmar("Você deseja remover este item?", "Você poderá adicionar ao carrinho pela tela do produto do mesmo depois, a não ser que o pedido já seja encomendado.",conf_remover_item_do_carrinho)
}

$(".remover-item").click(remover_item_do_carrinho);

function definir_horario_retirada() {
    let horario = $("#horario_retirada").val();
    erro = ""

    let time = new Date(horario).getTime()
    if (time===NaN) {
        erro += "Horário não foi definido (recarregue a página para ver o horário caso já tenha o definido)<br>";
    }
    if (time < new Date().getTime()) {
        erro += "Não pode se pedir com retirada no passado<br>";
    }
    else if (time < new Date().getTime()+1000*60*30) {
        erro += "A retirada não pode acontecer em menos de 30 minutos.";
    }

    if (erro != "") {
        mostrar_erro("Algo de errado ocorreu!",erro);
        return;
    }

    ajax("/definir_horario_retirada_ticket",{horario:time/1000}); // ms -> s
}

$("#btn-horario-retirada").click(definir_horario_retirada);

function conf_encaminhar_pedido() {
   ajax("/encaminhar_pedido",{},recarregar_no_sucesso);
}

function encaminhar_pedido() {
    confirmar("Deseja encaminhar seu pedido?","Revise seu pedido antes de encaminhar!");
}

$('#btn-encaminhar-pedido').click(encaminhar_pedido);
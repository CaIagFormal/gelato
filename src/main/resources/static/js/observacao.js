//Função de atualizar limite e conter texto dentro dele (1024 caracteres)
function atualiza_texto() {
    let texto = $('#obs-write').val().trimLeft();
    let limite = $('#lim-obs');

    if (texto.length>1023) {
        $('#obs-write').val(texto.substring(0,1023));
        limite.text('1023');
        return;
    }
    limite.text(texto.length);

    if ($('#obs-write').val()==texto) {return;}
    $('#obs-write').val(texto);
}

$('#obs-write').on('input',atualiza_texto)

//Função de salvar observação
function salvar_observacao() {
    let texto = $('#obs-write').val().trim();
    if (texto.trim()==0) {
        texto=null;
    }

    if (texto.length>1023) {
        mostrar_erro("Tamanho do texto excede 1023 caracteres.");
        return;
    }

    let url = "";
    if ($('#btn-salvar-obs').hasClass('ticket')) {
        url = "/observacao_ticket";
    }
    ajax(url,{texto:texto});
}

$('#btn-salvar-obs').click(salvar_observacao);

$('#obs-write').val($('#write-text-holder').text());
$('#write-text-holder').remove();
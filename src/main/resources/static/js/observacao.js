//Função de atualizar limite e conter texto dentro dele (1024 caracteres)
function atualiza_texto() {
    let texto = $('#obs-write').val();
    let limite = $('#lim-obs');

    if (texto.length>1023) {
        $('#obs-write').val(texto.substring(0,1023));
        limite.text('1023');
        return;
    }
    limite.text(texto.length);
}

$('#obs-write').on('input',atualiza_texto)

//Função de salvar observação
function salvar_observacao() {
    return;
}

$('#btn-salvar-obs').click(salvar_observacao)
function logout() {
    confirmar("Você deseja sair de sua conta?", "Terá de reenviar seus dados para acessar os recursos da página novamente.",conf_logout)
}
function conf_logout(a) {
    ajax("/fazer_logout",{},recarregar_no_sucesso);
}

$("#logout").click(logout);
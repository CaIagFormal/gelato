function logout() {
    swalWithBootstrapButtons.fire({
      title: "Você deseja sair de sua conta?",
      text: "Terá de reenviar seus dados para acessar os recursos da página novamente.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sim.",
      cancelButtonText: "Não.",
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        ajax("/logout",{},recarregar_no_sucesso);
      } else if (
        /* Read more about handling dismissals below */
        result.dismiss === Swal.DismissReason.cancel
      ) {
        swalWithBootstrapButtons.fire({
          title: "Como se nada tivesse acontecido...",
          text: "Você poderá continuar usando os recursos da página",
          icon: "info"
        });
      }
    });
}

$("#logout").click(logout);
const swalWithBootstrapButtons = Swal.mixin({
  customClass: {
    confirmButton: "btn btn-success m-2",
    cancelButton: "btn btn-danger m-2"
  },
  buttonsStyling: true
});

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
        swalWithBootstrapButtons.fire({
          title: "Saístes de sua conta",
          text: "Estás sendo redirecionado para a página de cadastro",
          icon: "success"
        });
        $("#logout_form").trigger("submit");
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
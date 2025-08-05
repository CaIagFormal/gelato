function mostrar_erro(titulo,erro) {
    Swal.fire({
      title: titulo,
      html: erro,
      icon: "error"
    });
}

const swalWithBootstrapButtons = Swal.mixin({
  customClass: {
    confirmButton: "btn btn-success m-2",
    cancelButton: "btn btn-danger m-2"
  },
  buttonsStyling: true
});
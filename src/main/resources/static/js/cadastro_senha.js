let lower = RegExp().compile("[a-z]+")
let upper = RegExp().compile("[A-Z]+")
let num = RegExp().compile("[0-9]+")
let special = RegExp().compile("[!-\/:-@[-`{-~]+") //MOHAMED YUSUFF

function conf_senha() {
    let senha = $("#senha").val().trim();

    fields = [false,false,false,false,false]
    fields[0] = (senha.length>7);
    fields[1] = (upper.test(senha));
    fields[2] = (lower.test(senha));
    fields[3] = (num.test(senha));
    fields[4] = (special.test(senha))
    for (let i=0; i<5; i++) {
        $("#senha_teste"+i).text((fields[i])?"✓":"⨂")
    }
}

$("#senha").on("input",conf_senha)

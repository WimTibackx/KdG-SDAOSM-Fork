//TODO - MOVE THIS TO DECENT CONFIG THINGY
var rootUrl="http://localhost:8080/BackEnd/";

$(document).ready(function() {
    console.log("HELLO");
    var $login = $("#loginform");
    $login.submit(function(e) {
        e.preventDefault();
        var username=$("[name=username]").val();
        var password=$("[name=password]").val();
        console.log(username);
        console.log("temp");
        console.log(password);
        actionLogin(username, password);
    });
});

function prepData(data) {
    return {data:JSON.stringify(data)};
}

function actionLogin(username, password) {

    var data={username:username, password:password};
    $.ajax({
        url:rootUrl+"login/",
        method:"POST",
        contentType: "text/plain; charset=utf-8",
        data:JSON.stringify(data),
        processData: false,
        success:function(response) {
            console.log(response);
        }
    })
}


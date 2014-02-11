//TODO - MOVE THIS TO DECENT CONFIG THINGY
var rootUrl="http://localhost:8080/BackEnd/";
$(document).ready(function() {
    var $login = $("#loginform");
    $login.submit(function(e) {
        e.preventDefault();
        var username=$login.children('[name="username"]').val();
        var password=$login.children('[name="password"]').val();
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
        method:"GET",
        data:prepData(data),
        success:function(response) {
            console.log(response);
        }
    })
}
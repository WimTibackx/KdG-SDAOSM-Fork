/**
 * Created by Thierry on 20/02/14.
 */
function clickLogout(){
    var rootUrl = "http://localhost:8080/BackEnd/";
    $.ajax({
        url: rootUrl + "authorized/logout",
        method: "GET",
        success: function (){
            console.log("He's logging out")
            window.location = "http://localhost:8080/frontend/app/index.html#";
            $("#error").text("");
            $("#error").show();
        }
    });
}
//Redefining this in the global scope because someone removed it before removing all references
//  and thus quite a lot of shit broke -_-
var carpoolingControllers = angular.module('carpoolingControllers', ['ui.bootstrap']);


function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function deleteActiveClass(){
    $('#MyProfileTab').removeClass('active');
    $('#SearchTab').removeClass('active');
    $('#PasswordTab').removeClass('active');
    $('#AboutTab').removeClass('active');
    $('#ContactTab').removeClass('active');
    $('#InboxTab').removeClass('active');
    $('#MyRitsTab').removeClass('active');
}

function deleteActiveTab(){

}
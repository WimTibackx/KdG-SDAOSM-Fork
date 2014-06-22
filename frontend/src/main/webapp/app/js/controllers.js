//Redefining this in the global scope because someone removed it before removing all references
//  and thus quite a lot of shit broke -_-
var carpoolingControllers = angular.module('carpoolingControllers', ['ui.bootstrap']);

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
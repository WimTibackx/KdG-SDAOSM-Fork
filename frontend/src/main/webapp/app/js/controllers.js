//Redefining this in the global scope because someone removed it before removing all references
//  and thus quite a lot of shit broke -_-
var rootUrl = "http://localhost:8080/BackEnd";

var carpoolingControllers = angular.module('carpoolingControllers', ['ui.bootstrap']);

/* Little experiment to load controllers
console.log('=== Begin Experiment ===');

var head = document.getElementsByTagName('head')[0];

var jsControllers = ['login', 'addCar', 'addRoute', 'changePassword', 'changeRemoveCar', 'myProfile', 'password', 'register', 'search'];

// Add name of controller file here when adding new controller (without Ctrl)
// So if you add "addCarCtrl.js" you add "addCar" to the array

for (var i = 0; i < jsControllers.length; i++) {
    var script = document.createElement('script');
    script.src = 'js/controllers/' + jsControllers[i] + 'Ctrl.js';
    head.appendChild(script);
}

console.log('=== End Experiment ===');
// End little experiment */

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
}
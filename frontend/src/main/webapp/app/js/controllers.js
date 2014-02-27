//Redefining this in the global scope because someone removed it before removing all references
//  and thus quite a lot of shit broke -_-
var rootUrl = "http://localhost:8080/BackEnd";

var carpoolingControllers = angular.module('carpoolingControllers', ['ui.bootstrap']);

// Little experiment to load controllers
console.log('=== Begin Experiment ===');

var head = document.getElementsByTagName('head')[0];
var jsControllers = ['login', 'addCar', 'addRoute', 'changePassword', 'changeRemoveCar', 'myProfile', 'password', 'register'];
// Add name of controller file here when adding new controller (without Ctrl)
// So if you add "addCarCtrl.js" you add "addCar" to the array

for (var i = 0; i < jsControllers.length; i++) {
    var script = document.createElement('script');
    script.src = 'js/controllers/' + jsControllers[i] + 'Ctrl.js';
    head.appendChild(script);
}

console.log('=== End Experiment ===');
// End little experiment

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

function addCarImage(insertedCar) {
    var file = document.getElementById('image').files[0];
    var data = new FormData();
    data.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/BackEnd/authorized/user/car/" + insertedCar + "/uploadphoto");
    xhr.send(data);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var jsonResponse = JSON.parse(xhr.responseText);
            if (jsonResponse.hasOwnProperty("error")) {
                console.log(jsonResponse.error);
                if (jsonResponse.error == "CarNotFound") {
                    $("#error").html("There was a problem with the car");
                } else if (jsonResponse.error == "CarNotYours") {
                    //TODO
                } else if (jsonResponse.error == "ImageError") {
                    //TODO
                }
            } else if (jsonResponse.hasOwnProperty("url")) {
                $("#imgpreview").html('<img src="http://localhost:8080/BackEnd/carImages/' + jsonResponse.url + '" alt="The car" width="12em" />');
            }
        }
    }
}

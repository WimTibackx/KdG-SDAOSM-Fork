var carpoolingControllers = angular.module('carpoolingControllers', []);
var token;


carpoolingControllers.controller('loginCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    var rootUrl = "http://localhost:8080/BackEnd/";


    console.log("HELLOOOOOO");
    console.log(readCookie("Token"));
    if (readCookie("Token") == null){
        $("#cssmenu").hide();

        $(document).ready(function () {
            var login = $("#loginform");
            login.submit(function (e) {
                e.preventDefault();
                var username=$("[name=username]").val();
                var password=$("[name=password]").val();
                actionLogin(username, password);
            });
        });

        function actionLogin(username, password) {
            var data = {username: username, password: password};
            $http({
                method: 'POST',
                url: rootUrl + "login/",
                data: JSON.stringify(data),
                headers: {'Content-Type': "text/plain; charset=utf-8"}
            }).success(function (response) {
                    console.log(response)
                    //var obj = JSON.parse(response);
                    obj = response;
                    console.log(obj);
                    if(obj.hasOwnProperty("Token")){
                        console.log("Test");
                        window.location = "http://localhost:8080/frontend/app/index.html#/myProfile";
                        $("#cssmenu").show();
                        $("#error").hide();
                    }else if (obj.hasOwnProperty("error")) {

                        if(obj["error"] == "LoginComboWrong") {
                            $("#error").text("Combination username/password is wrong");
                            $("#error").show();
                            $("#cssmenu").hide();

                        }
                        if(obj["error"] == ("ParseError")) {
                            $("#error").text("There is a problem with our server, please try again later");
                            $("#error").show();
                            $("#cssmenu").hide();
                        }
                    }
                });
            /*$.ajax({
                url: rootUrl + "login/",
                method: "POST",
                contentType: "text/plain; charset=utf-8",
                data:JSON.stringify(data),
                success: function (response) {
                    var obj = JSON.parse(response);
                    console.log(obj);
                    if(obj.hasOwnProperty("Token")){
                        console.log("Test");
                        window.location = "http://localhost:8080/frontend/app/index.html#/myProfile";
                        $("#cssmenu").show();
                        $("#error").hide();
                    }else if (obj.hasOwnProperty("error")) {

                        if(obj["error"] == "LoginComboWrong") {
                            $("#error").text("Combination username/password is wrong");
                            $("#error").show();
                            $("#cssmenu").hide();

                        }
                        if(obj["error"] == ("ParseError")) {
                            $("#error").text("There is a problem with our server, please try again later");
                            $("#error").show();
                            $("#cssmenu").hide();
                        }
                    }
                }
            })    */
        }
    }else{

        window.location = "http://localhost:8080/frontend/app/index.html#/myProfile";
    }

}]);

carpoolingControllers.controller('myProfileCtrl', ['$scope', '$http', function ($scope, $http) {
    $scope.avatarsrc = '../app/img/avatar.JPG';
    $scope.gendersrc = '../app/img/female.png';

    var rootUrl = "http://localhost:8080/BackEnd/";


    var username = null;
    $http({
        method: 'GET',
        url: rootUrl + "authorized/myprofile/",
        headers: {'Content-Type': "text/plain; charset=utf-8"}
    }).success(function (response) {
            obj = response;
            console.log(obj)
            if(obj.hasOwnProperty("error")){
                if(obj["error"] == "AuthorizationNeeded") {
                    window.location = "http://localhost:8080/frontend/app/index.html#/login";
                    $("#error").text("Authorization needed, please log in");
                    $("#error").show();
                }
            }else {
                username = obj["name"];
                $scope.personname = username
                var date = obj["dateOfBirth"];
                $scope.dateBirth = date["day"] + "/" + date["month"] + "/" + date["year"];
                $scope.cars = obj["cars"];
                console.log(JSON.stringify(obj["dateOfBirth"]))



            }


        });

    /*$.ajax({

        url: rootUrl + "authorized/myprofile/",
        method: "GET",
        success: function (response, $scope) {
            console.log(response);
            var obj = JSON.parse(response);
            console.log(obj)  ;
            if(obj.hasOwnProperty("error")){
                if(obj["error"] == "AuthorizationNeeded") {
                    window.location = "http://localhost:8080/frontend/app/index.html#/login";
                    $("#error").text("Authorization needed, please log in");
                    $("#error").show();
                }
            }else {
                username = obj["name"];
                console.log(String("Hij komt hier"));


                $scope.$apply(function(){
                    $scope.personname = "Test";
                });


            }


        }
    });

    console.log(String(username));
    $scope.personname = String(username);      */


}]);


function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}
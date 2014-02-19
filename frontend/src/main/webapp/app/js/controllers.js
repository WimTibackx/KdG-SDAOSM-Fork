var carpoolingControllers = angular.module('carpoolingControllers', []);
var token;


carpoolingControllers.controller('loginCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    var rootUrl = "http://localhost:8080/BackEnd/";


    if ($.cookie("Token") == null){
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
            $.ajax({
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
            })
        }
    }else{
        window.location = "http://localhost:8080/frontend/app/index.html#/myProfile";
    }

}]);

carpoolingControllers.controller('myProfileCtrl', ['$scope', '$http', function ($scope, $http) {
    $scope.avatarsrc = '../app/img/avatar.JPG';
    $scope.gendersrc = '../app/img/female.png';

    var rootUrl = "http://localhost:8080/BackEnd/";
    $.ajax({
        url: rootUrl + "authorized/myprofile/",
        method: "GET",
        success: function (response) {
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
                $scope.name = obj["name"] ;

            }


        }
    });



}]);

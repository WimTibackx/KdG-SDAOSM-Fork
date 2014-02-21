var carpoolingControllers = angular.module('carpoolingControllers', []);
var token;

carpoolingControllers.controller('registerCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    console.log("hey test register ctrl");

    var rootUrl = "http://localhost:8080/BackEnd/";

    $(document).ready(function () {
        var registerform = $("#registerform");

        registerform.submit(function (e) {
            console.log("registration completed");
            e.preventDefault();
            var usernameR = $("[name=usernameRegister]").val();
            var passwordR = $("[name=passwordRegister]").val();
            var nameR = $("[name=nameRegister]").val();
            var smokerR = $("[name=smokerRegister]").val();
            var genderR = $("[name=genderRegister]").val();
            var dateofbirthR = $("[name=dateofbirthRegister]").val();

            actionRegister(usernameR, passwordR, nameR, smokerR, genderR, dateofbirthR);
        });

        function actionRegister(usernameR, passwordR, nameR, smokerR, genderR, dateofbirthR) {
            var data = {username: usernameR, password: passwordR, name: nameR, smoker: smokerR, gender: genderR, dateofbirth: dateofbirthR};
            $.ajax({
                url: rootUrl + "login/",
                method: "POST",
                contentType: "text/plain; charset=utf-8",
                data: JSON.stringify(data),
                success: function (response) {
                    var obj = JSON.parse(response);
                    console.log(obj);
                    if (obj.hasOwnProperty("Token")) {
                        console.log("Test");
                        window.location = "http://localhost:8080/frontend/app/index.html#/myProfile";
                        $("#cssmenu").show();
                        $("#error").hide();
                    } else if (obj.hasOwnProperty("error")) {

                        if (obj["error"] == "RegisterWrong") {
                            $("#error").text("One of the fiels is wrong");
                            $("#error").show();
                            $("#cssmenu").hide();

                        }
                        if (obj["error"] == ("ParseError")) {
                            $("#error").text("There is a problem with our server, please try again later");
                            $("#error").show();
                            $("#cssmenu").hide();
                        }
                    }
                }
            })
            console.log("end registration");
        }
    })
}]);


carpoolingControllers.controller('loginCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    var rootUrl = "http://localhost:8080/BackEnd/";

    console.log("hey test login ctrl");
    console.log(readCookie("Token"));

    if (readCookie("Token") == null) {

        var login = document.getElementById('login');
        var menu = document.getElementById('cssmenu');
        menu.style.display = 'none';
        var register = document.getElementById('register');
        var password = document.getElementById('password');
        document.getElementById('loginIcon').addEventListener('click', function () {
            login.style.display = 'block';
            register.style.display = 'none';
            password.style.display = 'none';
        });
        document.getElementById('registerIcon').addEventListener('click', function () {
            register.style.display = 'block';
            password.style.display = 'none';
            login.style.display = 'none';
        });
        document.getElementById('passwordIcon').addEventListener('click', function () {
            password.style.display = 'block';
            register.style.display = 'none';
            login.style.display = 'none';
        });


        $("#cssmenu").hide();

        $(document).ready(function () {
            var login = $("#loginform");


            login.submit(function (e) {
                e.preventDefault();
                var username = $("[name=username]").val();
                var password = $("[name=password]").val();

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
                    if (obj.hasOwnProperty("Token")) {
                        console.log("Test");
                        window.location = "http://localhost:8080/frontend/app/index.html#/myProfile";
                        $("#cssmenu").show();
                        $("#error").hide();
                    } else if (obj.hasOwnProperty("error")) {

                        if (obj["error"] == "LoginComboWrong") {
                            $("#error").text("Combination username/password is wrong");
                            $("#error").show();
                            $("#cssmenu").hide();

                        }
                        if (obj["error"] == ("ParseError")) {
                            $("#error").text("There is a problem with our server, please try again later");
                            $("#error").show();
                            $("#cssmenu").hide();
                        }
                    }
                });

        }

    } else {
        window.location = "http://localhost:8080/frontend/app/index.html#/myProfile";
    }
}
])
;

carpoolingControllers.controller('passwordCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    console.log("hey password controller test");

    var password = $('#passwordform');

    password.submit(function (e) {
        console.log("start mailing...");
        var link = "mailto:" + document.getElementById('emailadres') + "?cc=melissa.warrens@student.kdg.be" + "&subject=password forget" + "&body=your password is: test";
        console.log("end mailing...");
        window.location.href = link;
    });
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
            if (obj.hasOwnProperty("error")) {
                if (obj["error"] == "AuthorizationNeeded") {
                    console.log("He is not authorized");
                    window.location = "http://localhost:8080/frontend/app/index.html#/login";
                    $("#error").text("Authorization needed, please log in");
                    $("#error").show();
                }
            } else {
                username = obj["name"];
                $scope.personname = username
                var date = obj["dateOfBirth"];
                $scope.dateBirth = date["day"] + "/" + date["month"] + "/" + date["year"];
                $scope.cars = obj["cars"];
                $scope.username = obj["username"]
                if (obj["smoker"]) {
                    $scope.smoker = "Smoker"
                } else {
                    $scope.smoker = "Non smoker"
                }
                console.log(JSON.stringify(obj["dateOfBirth"]))


            }


        });


    $scope.removeCar = function (carId) {
        console.log(carId);
    }

}]);


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

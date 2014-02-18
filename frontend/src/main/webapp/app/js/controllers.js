var carpoolingControllers = angular.module('carpoolingControllers', []);
var token;

carpoolingControllers.controller('loginCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    var rootUrl = "http://localhost:8080/BackEnd/";

    $(document).ready(function () {
        var login = $("#loginform");
        login.submit(function (e) {
            e.preventDefault();
            var username = login.children('[name="username"]').val();
            var password = login.children('[name="password"]').val();
            actionLogin(username, password);
        });
    });

    function prepData(data) {
        return {data: JSON.stringify(data)};
    }

    function actionLogin(username, password) {
        var data = {username: username, password: password};
        $.ajax({
            url: rootUrl + "login/",
            method: "POST",
            data: prepData(data),
            success: function (response) {
                //console.log(response);
                token = response;
                receivedToken(token);
            }
        })
    }

    function receivedToken(token){
        $location.url('/myProfile');
    }
}]);

carpoolingControllers.controller('myProfileCtrl', ['$scope', '$http', function ($scope, $http) {
    $scope.avatarsrc = '../app/img/avatar.JPG';
    $scope.gendersrc = '../app/img/female.png';
}]);

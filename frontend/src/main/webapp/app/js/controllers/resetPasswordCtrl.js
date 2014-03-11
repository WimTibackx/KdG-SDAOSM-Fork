carpoolingApp.controllerProvider.register('resetPasswordCtrl', ['$scope', '$http', '$location', '$api', function ($scope, $http, $location, $api) {
    console.log("hey reset password controller test");

    $scope.resetPwd = function () {
        var jsonObject = {};
        jsonObject.username = $scope.usernameEmail;
        console.log(jsonObject);

        $http.post(rootUrl + "/authorized/changepassword/reset", JSON.stringify(jsonObject)).success(function () {
            console.log("reset password succes")
        }).
            error(function () {
                console.log("an error occured");
            });
    }
}]);
/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: Password
carpoolingApp.controllerProvider.register('passwordCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    $scope.PWRError = "No error";
    $scope.displayPWRError = false;

    var sendPassword = $('#sendPasswordForm');
    $scope.sendPassword = function (e) {
        var jsonObject = {};
        jsonObject.username = $scope.usernameEmail;
        $scope.displayPWRFeedback = true;
        console.log(JSON.stringify(jsonObject));

        $http.post(rootUrl + "/resetPassword", JSON.stringify(jsonObject)).success(function (result) {
            if (!result.hasOwnProperty("error")) {
                console.log("New password sent to " + jsonObject.username);
                $scope.PWRClass = 'success';
                $scope.PWRFeedback = 'New password sent to ' + jsonObject.username;
            } else {
                console.log("Error: " + result.error);
                $scope.PWRClass = 'error';
                $scope.PWRFeedback = result.error;

            }
            console.log(result);
            //console.log("New password sent to: " + jsonObject.username);
        }).
            error(function () {
                console.log("An error occured");
            });
    };
}]);
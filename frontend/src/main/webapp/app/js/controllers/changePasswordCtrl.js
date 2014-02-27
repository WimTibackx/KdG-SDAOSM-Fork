
/**
 * Created by peter on 25/02/14.
 */
// Change Password Controller
carpoolingControllers.controller('changePasswordCtrl', ['$scope', '$http', '$location', '$api', function ($scope, $http, $location, $api) {
    console.log("hey changePassword controller test");

    $scope.changePwd = function () {

        if ($scope.newpwd1 == $scope.newpwd2) {
            jsonObj = {};
            jsonObj.oldpassword = $scope.oldpwd;
            jsonObj.newpassword = $scope.newpwd1;

            $http({
                method: 'POST',
                url: rootUrl + "/authorized/changepassword",
                data: JSON.stringify(jsonObj)
            }).success(function (response) {

                    console.log(response);

                })
                .error(function(response){
                    console.log("Hij komt in error")
                    $scope.error = "Onze service is niet beschikbaar, vul alle velden zeker in";
                })

                    console.log("Password change success");
                    window.location.href = "http://localhost:8080/frontend/app/index.html#/myProfile";


        } else {
            $scope.error = "Wachtwoorden komen niet overeen."
        }
    }
}]);
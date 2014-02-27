
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
                    if (obj.hasOwnProperty("result")){
                        if(obj["error"] == "PasswordChanged") {
                            $scope.hideError = true;
                            $scope.hideMessage = false;
                            $scope.message = "Wachtwoord is succesvol aangepast"
                        }else if(obj["error"] == "OldPasswordWrong"){
                            $scope.hideError = true;
                            $scope.hideMessage = false;
                            $scope.message = "Wachtwoord is succesvol aangepast"

                        }else if(obj["error"] == "NewPasswordFormatWrong"){
                            $scope.hideError = true;
                            $scope.hideMessage = false;
                            $scope.message = "Wachtwoord formaat is verkeerd, 1 Hoofdletter, 1 kleine letter en tussen de 7 en 30 lang"

                        }

                    }

                })
                .error(function(response){

                    $scope.error = "Onze service is niet beschikbaar, vul alle velden zeker in";
                    $(".error").show();
                })
        } else {
            $scope.error = "Wachtwoorden komen niet overeen."
            $(".error").show();
        }
    }
}]);
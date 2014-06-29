/**
 * Created by peter on 25/02/14.
 */
// Change Password Controller
carpoolingApp.controllerProvider.register('changePasswordCtrl', ['$scope', '$location', '$http', function ($scope, $location, $http) {

    $scope.PasswordSubmit = function () {

        if ($scope.newpwd1 == $scope.newpwd2) {
            jsonObj = {};
            jsonObj.oldpassword = $scope.oldpwd;
            jsonObj.newpassword = $scope.newpwd1;


            $http({
                method: 'POST',
                url: rootUrl + "/authorized/changepassword",
                data: JSON.stringify(jsonObj)
            }).success(function (response) {

                    if (response.hasOwnProperty("result")) {
                        if (response["result"] == "PasswordChanged") {
                            $scope.hideError = true;
                            $scope.hideMessage = false;
                            $scope.message = "Wachtwoord is succesvol aangepast"
                        } else if (response["result"] == "OldPasswordWrong") {
                            $scope.hideError = false;
                            $scope.hideMessage = true;
                            $scope.error = "Het oude wachtwoord is onjuist"

                        } else if (response["result"] == "NewPasswordFormatWrong") {
                            $scope.hideError = false;
                            $scope.hideMessage = true;;

                            $scope.error = "Wachtwoord formaat is verkeerd, 1 Hoofdletter, 1 kleine letter en tussen de 7 en 30 lang"

                        }

                    }

                })
                .error(function (response) {

                    $scope.error = "Onze service is niet beschikbaar, vul alle velden zeker in";
                    $(".error").show();
                    $(".message").hide();
                })


        } else {
            $scope.error = "Wachtwoorden komen niet overeen."
            $(".error").show();
        }
    }
}]);

/**
 * Created by peter on 25/02/14.
 */
// Change Password Controller
carpoolingApp.controllerProvider.register('changePasswordCtrl', ['$scope', '$http', '$location', '$api', function ($scope, $http, $location, $api) {
    console.log("hey changePassword controller test");

    $scope.changePwd = function () {

        if ($scope.newpwd1 == $scope.newpwd2) {
            jsonObj = {};
            jsonObj.oldpassword = $scope.oldpwd;
            jsonObj.newpassword = $scope.newpwd1;


            // $api.post('/authorized/changepassword');
            $http({
                method: 'POST',
                url: rootUrl + "/authorized/changepassword",
                data: JSON.stringify(jsonObj)
            }).success(function (response) {

                    console.log(response);
                    if (response.hasOwnProperty("result")){
                        if(response["result"] == "PasswordChanged") {
                            $(".error").hide();
                            $(".message").show();
                            $scope.message = "Wachtwoord is succesvol aangepast"
                        }else if(response["result"] == "OldPasswordWrong"){
                            $(".error").show();
                            $(".message").hide();
                            $scope.error = "Het oude wachtwoord is onjuist"

                        }else if(response["result"] == "NewPasswordFormatWrong"){
                            $(".error").show();
                            $(".message").hide();

                            $scope.error = "Wachtwoord formaat is verkeerd, 1 Hoofdletter, 1 kleine letter en tussen de 7 en 30 lang"

                        }

                    }

                })
                .error(function(response){

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
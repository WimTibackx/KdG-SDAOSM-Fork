/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: Login
carpoolingApp.controllerProvider.register('loginCtrl', ['$scope', '$http', '$location', 'SharedProperties', function ($scope, $http, $location, SharedProperties) {

    console.log("hey test login ctrl");
    console.log(readCookie("Token"));
    if (SharedProperties.getProperty() != null) {
        $("#error").text(SharedProperties.getProperty());
        $("#error").show();
        SharedProperties.setProperty(null);
    }
    console.log(SharedProperties.getProperty());
    if (readCookie("Token") == null) {

        var login = document.getElementById('login');
        var menu = document.getElementById('cssmenu');
        menu.style.display = 'none';
        var register = document.getElementById('register');
        var password = document.getElementById('password');

        /* document.getElementById('loginIcon').addEventListener('click', function () {
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
        }); */

        $(document).ready(function () {

            var login = $("#loginform");
            console.log("Hij komt in doc.ready function")

            login.submit(function (e) {
                e.preventDefault();
                var username = $("[name=username]").val();
                var password = $("[name=password]").val();

                actionLogin(username, password);
            });

            $scope.PWRError = "No error";
            $scope.displayPWRError = false;

            var sendPassword = $('#sendPasswordForm');
            sendPassword.submit(function (e) {
                var jsonObject = {};
                jsonObject.username = $scope.usernameEmail;
                $scope.displayPWRFeedback = true;

                $http.post(rootUrl + "/resetPassword", JSON.stringify(jsonObject)).success(function (result) {
                    if(!result.hasOwnProperty("error")) {
                        console.log("New password sent to " + jsonObject.username);
                        $scope.PWRClass = 'success';
                        $scope.PWRFeedback = 'New password sent to ' + jsonObject.username;
                    } else {
                        console.log("Error: " + result.error);
                        $scope.PWRClass = 'error';
                        $scope.PWRFeedback = result.error;

                    }
                    console.log(result);
                    //console.log("New password sended to: " + jsonObject.username);
                }).
                    error(function () {
                        console.log("An error occured");
                    });
            });
        });

        function actionLogin(username, password) {
            console.log("Hij komt in action login function")
            var data = {username: username, password: password};
            var counter = 0;
            $http({
                method: 'POST',
                url: rootUrl + "/login/",
                data: JSON.stringify(data),
                headers: {'Content-Type': "text/plain; charset=utf-8"}
            }).success(function (response) {

                    console.log(response)
                    //var obj = JSON.parse(response);
                    obj = response;
                    console.log(obj);
                    if (obj.hasOwnProperty("Token")) {
                        console.log("Test");
                        $location.path("/myProfile");
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

        console.log("Test gij komt hier")

    } else {
        console.log("It's because of this ??");
        $location.path("/myProfile");
    }
}
]);
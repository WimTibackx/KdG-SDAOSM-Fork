/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: Login
carpoolingControllers.controller('loginCtrl', ['$scope', '$http', '$location', 'SharedProperties', function ($scope, $http, $location, SharedProperties) {

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

        $(document).ready(function () {

            var login = $("#loginform");
            console.log("Hij komt in doc.ready function")

            login.submit(function (e) {
                e.preventDefault();
                var username = $("[name=username]").val();
                var password = $("[name=password]").val();

                actionLogin(username, password);
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
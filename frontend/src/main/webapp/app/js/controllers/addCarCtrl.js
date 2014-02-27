/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: Add car
carpoolingApp.controllerProvider.register('addCarCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    var insertedCar = -1;

    if (readCookie("Token") == null) {
        window.location = "http://localhost:8080/frontend/app/index.html#/login";
        return;
    }

    $(document).ready(function () {

        $("#addcarform").submit(function (e) {
            e.preventDefault();
            var data = {
                brand: $("#addcarform input#brand").val(),
                type: $("#addcarform input#type").val(),
                fueltype: $("#addcarform select#fueltype").val(),
                consumption: $("#addcarform input#consumption").val()
            };

            $http({
                method: 'POST',
                url: rootUrl + "/authorized/user/car/add/",
                data: JSON.stringify(data),
                headers: {'Content-Type': "text/plain; charset=utf-8"}
            }).success(function (response) {
                    if (response.hasOwnProperty("inserted")) {
                        insertedCar = response.inserted;
                        console.log("We can proceed to uploading ze photo");
                        $("#addcardata").css("display", "none");
                        $("#addcarimage").css("display", "block");
                    } else if (response.hasOwnProperty("error")) {
                        console.log("We had an error!");
                        $("#addcarform #error").html("We had an error!");
                    }
                });
        });

        $("#addcarimage #upload").click(function () {
            var file = document.getElementById('image').files[0];
            var data = new FormData();
            data.append("file", file);

            var xhr = new XMLHttpRequest();
            xhr.open("POST", "http://localhost:8080/BackEnd/authorized/user/car/" + insertedCar + "/uploadphoto");
            xhr.send(data);
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    var jsonResponse = JSON.parse(xhr.responseText);
                    if (jsonResponse.hasOwnProperty("error")) {
                        console.log(jsonResponse.error);
                        if (jsonResponse.error == "CarNotFound") {
                            $("#error").html("There was a problem with the car");
                        } else if (jsonResponse.error == "CarNotYours") {
                            //TODO
                        } else if (jsonResponse.error == "ImageError") {
                            //TODO
                        }
                    } else if (jsonResponse.hasOwnProperty("url")) {
                        $("#imgpreview").html('<img src="http://localhost:8080/BackEnd/carImages/' + jsonResponse.url + '" alt="The car" width="12em" />');
                    }
                }
            }
        });
    });
}]);
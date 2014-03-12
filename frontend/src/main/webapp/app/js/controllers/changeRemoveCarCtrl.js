/**
 * Created by peter on 25/02/14.
 */
// ChangeRemoveCar Controller
carpoolingApp.controllerProvider.register('changeRemoveCarCtrl', ['$scope', '$routeParams', '$http', function ($scope, $routeParams, $http) {

    deleteActiveClass();
    $('#MyProfileTab').addClass('active');

    var id = $routeParams.carId;

    $scope.carChange = id;

    $http({
        method: 'GET',
        url: rootUrl + "/authorized/user/car/" + id + "/",
        headers: {'Content-Type': "text/plain; charset=utf-8"}
    }).success(function (response) {
            console.log(response);
            //TODO: error handling

            $scope.carBrand = response.brand;
            $scope.carType = response.type;
            $scope.carConsumption = response.consumption;

            switch (response.fuelType) {
                case "SUPER95":
                    document.getElementById("super95").checked = true;
                    break;
                case "SUPER98":
                    document.getElementById("super98").checked = true;
                    break;
                case "DIESEL":
                    document.getElementById("diesel").checked = true;
                    break;
            }

            /*if (response.hasOwnProperty("inserted")) {
             insertedCar = response.inserted;
             console.log("We can proceed to uploading ze photo");
             $("#addcardata").css("display", "none");
             $("#addcarimage").css("display", "block");
             } else if (response.hasOwnProperty("error")) {
             console.log("We had an error!");
             $("#addcarform #error").html("We had an error!");
             }   */
        });

    // buttons

    $("#addcarimage #upload").click(function () {
        $scope.$apply(function () {
            var file = document.getElementById('image').files[0];
            var data = new FormData();
            data.append("file", file);

            var xhr = new XMLHttpRequest();
            xhr.open("POST", "http://localhost:8080/BackEnd/authorized/user/car/" + id + "/uploadphoto");
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
        })
    });


    $scope.changeCarSpecs = function () {
        console.log("change car clicked");

        var jsonObject = {};

        var brand = document.getElementById("carNameInput").value;
        console.log(brand);
        var type = document.getElementById("carType").value;
        console.log(type);
        var consumption = document.getElementById("carConsumption").value;
        console.log(consumption);

        var fuels = document.getElementsByName("fuel");
        var fuel = "";
        for (var i = 0; i < fuels.length; i++) {
            if (fuels[i].checked) fuel = fuels[i].value;
        }
        console.log(fuel);

        jsonObject.brand = brand;
        jsonObject.type = type;
        jsonObject.consumption = consumption;
        jsonObject.fueltype = fuel;

        console.log(jsonObject);

        //send to backend
        $http({
            method: 'POST',
            url: rootUrl + "/authorized/user/car/" + id + "/update",
            data: JSON.stringify(jsonObject),
            headers: {'Content-Type': "text/plain; charset=utf-8"}
        }).success(function (response) {
                console.log("Car specs changed");
                window.location.href = "http://localhost:8080/frontend/app/index.html#/myProfile";
            });
    }
    $scope.deleteCar = function () {
        console.log("delete car clicked");

        $http({
            method: 'POST',
            url: rootUrl + "/authorized/user/car/" + id + "/delete",
            headers: {'Content-Type': "text/plain; charset=utf-8"}
        }).success(function (response) {
                console.log("Car deleted");
                window.location.href = "http://localhost:8080/frontend/app/index.html#/myProfile";
            });
    }
}]);
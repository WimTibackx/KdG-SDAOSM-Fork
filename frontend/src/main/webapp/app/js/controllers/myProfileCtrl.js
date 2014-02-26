/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: My profile
carpoolingControllers.controller('myProfileCtrl', ['$scope', '$http', '$location', 'SharedProperties', function ($scope, $http, $location, SharedProperties) {
    $scope.avatarsrc = '../app/img/avatar.JPG';
    $scope.gendersrc = '../app/img/female.png';
    //$scope.carPicture = 'http://localhost8080:BackEnd/carImages/';
    var counter = 0;
    var username = null;
    $http({
        method: 'GET',
        url: rootUrl + "/authorized/myprofile/",
        headers: {'Content-Type': "text/plain; charset=utf-8"}
    }).success(function (response) {
            obj = response;
            console.log(obj)
            var temp;
            if (obj.hasOwnProperty("error")) {
                if (obj["error"] == "AuthorizationNeeded") {
                    console.log("He is not authorized");
                    $location.path("/login");
                    SharedProperties.setProperty("Authorization is required")

                }
            } else {
                username = obj["name"];
                $scope.personname = username;
                var date = obj["dateOfBirth"];
                $scope.dateBirth = date["day"] + "/" + date["month"] + "/" + date["year"];
                $scope.cars = obj["cars"];
                $scope.username = obj["username"]
                if (obj["smoker"]) {
                    $scope.smoker = "Smoker"
                } else {
                    $scope.smoker = "Non smoker"
                }
                console.log(JSON.stringify(obj["dateOfBirth"]))
            }
        });

    $scope.hideRoutes = true;
    $scope.hideTrajects = true;

    $scope.removeCar = function (carId) {
        console.log(carId);
    }

    $scope.clickCar = function (car) {
        console.log(car.carId);
        window.location.href = "http://localhost:8080/frontend/app/index.html#/myProfile/changeRemoveCar/" + car.carId + "";
    }

    $scope.tabRoutesClick = function () {
        $scope.hideCars = true;
        $scope.hideRoutes = false;
        $scope.hideTrajects = true;
    }
    $scope.tabCarsClick = function () {
        $scope.hideCars = false;
        $scope.hideRoutes = true;
        $scope.hideTrajects = true;
    }
    $scope.tabTrajectsClick = function () {
        $scope.hideCars = true;
        $scope.hideRoutes = true;
        $scope.hideTrajects = false;
    }
}]);
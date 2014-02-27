/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: My profile
carpoolingApp.controllerProvider.register('myProfileCtrl', ['$scope', '$http', '$location', 'SharedProperties', function ($scope, $http, $location, SharedProperties) {
    $scope.avatarsrc = '../app/img/avatar.JPG';

    //$scope.carPicture = 'http://localhost8080:BackEnd/carImages/';
    var counter = 0;
    var username = null;
    var gender = null;

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
                gender = obj["gender"];
                if(gender == ("FEMALE")){
                    $scope.gendersrc = '../app/img/female.png';
                } else{
                    $scope.gendersrc = '../app/img/male.png';
                }
                $scope.personname = username;
                var date = obj["dateOfBirth"];
                $scope.dateBirth = date["day"] + "/" + date["month"] + "/" + date["year"];
                $scope.cars = obj["cars"];
                $scope.username = obj["username"]
                if (obj["smoker"]) {
                    $scope.smoker = "Roker"
                } else {
                    $scope.smoker = "Geen roker"
                }
                console.log(JSON.stringify(obj["dateOfBirth"]))
            }
        });

    $scope.hideRoutes = true;
    $scope.hideTrajects = true;
    $scope.addText = "Voeg auto toe";

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
        $scope.addText = "Voeg route toe";
    }
    $scope.tabCarsClick = function () {
        $scope.hideCars = false;
        $scope.hideRoutes = true;
        $scope.hideTrajects = true;
        $scope.addText = "Voeg auto toe";
    }
    $scope.tabTrajectsClick = function () {
        $scope.hideCars = true;
        $scope.hideRoutes = true;
        $scope.hideTrajects = false;
        $scope.addText = "Voeg traject toe";
    }
}]);
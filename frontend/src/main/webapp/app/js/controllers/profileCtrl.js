/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: My profile
carpoolingApp.controllerProvider.register('profileCtrl', ['$scope', '$http', '$location', 'SharedProperties', "$authChecker", '$routeParams', function ($scope, $http, $location, SharedProperties, $authChecker, $routeParams) {
    deleteActiveClass();
    $('#MyProfileTab').addClass('active');

    $authChecker.checkAuthorization();
    var userId = $routeParams.userId;


    $scope.routes=[];

    //$scope.carPicture = 'http://localhost8080:BackEnd/carImages/';
    var counter = 0;
    var username = null;
    var gender = null;
    var avatarUrl = null ;

    $http.get(rootUrl + "/authorized/profile/"+userId).success(function (response) {
        obj = response;
        console.log(obj);
        var temp;
        if (obj.hasOwnProperty("error")) {
            if (obj["error"] == "AuthorizationNeeded") {
                console.log("He is not authorized");
                $location.path("/login");
                SharedProperties.setProperty("Authorization is required")

            }
        } else {
            loadRoutes();
            username = obj["name"];
            gender = obj["gender"];
            if (obj.hasOwnProperty("avatarURL")) {
                avatarUrl = obj["avatarURL"]
            }
            if(gender == ("FEMALE")){
                $scope.gendersrc = '../app/img/female.png';
            } else{
                $scope.gendersrc = '../app/img/male.png';
            }
            $scope.personname = username;
            $scope.dateBirth = new Date(obj["dateOfBirth"]);
            $scope.cars = obj["cars"];
            console.log($scope.cars);
            $scope.username = obj["username"];
            if (obj["smoker"]) {
                $scope.smoker = "Roker"
            } else {
                $scope.smoker = "Geen roker"
            }
            console.log(JSON.stringify(obj["dateOfBirth"]))
        }
    });

    $scope.removeCar = function (carId) {
        console.log(carId);
    };

    $scope.clickCar = function (car) {
        console.log(car.carId);
        window.location.href = "http://localhost:8080/frontend/app/index.html#/myProfile/changeRemoveCar/" + car.carId + "";
    };

    $scope.tabRoutesClick = function () {
        $scope.hideCars = true;
        $scope.hideRoutes = false;
    };
    $scope.tabCarsClick = function () {
        $scope.hideCars = false;
        $scope.hideRoutes = true;
    };

    $scope.goRoute = function(id,day) { $location.path("/route/"+id).hash(day); };

    $scope.formatDate = function(date) {
        if (date == undefined) return "";
        return date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getFullYear();
    };
    $scope.formatDateString = function(dateString) {
        var date = new Date(dateString);
        return $scope.formatDate(date);
    };
    $scope.booleanL10n = function(b) { return b ? "Ja" : "Nee"; };

    function loadRoutes() {
        $http.get(rootUrl+"/authorized/route/user/"+userId).success(function (data) {
            $scope.routes=data;
        });
    }

    $scope.getAvatarUrl=function(){
        console.log("Test ");
        if (avatarUrl != null){
            return "http://localhost:8080/BackEnd/userImages/"+avatarUrl;
        }else{
            if (gender == "FEMALE"){
                return "../app/img/femaleAvatar.png";
            }else {
                return "../app/img/maleAvatar.png";
            }
        }
    };

    $scope.getCarImageUrl=function(part) {
        if (part != null){
            return "http://localhost:8080/BackEnd/carImages/"+part;
        }else{
            return "../app/img/carAvatar.jpg"
        }

    };


    $scope.getWeekdayL10n=function(weekdayNum) {
        var weekdaysL10n=["Maandag","Dinsdag","Woensdag","Donderdag","Vrijdag","Zaterdag","Zondag"];
        if (weekdayNum == undefined) return weekdaysL10n[0];
        return weekdaysL10n[weekdayNum];
    };

    function determineActiveTabAtPageload() {
        switch ($location.hash()) {
            case "cars":
            default:
                $scope.tabCarsClick();
                break;
            case "routes":
                $scope.tabRoutesClick();
                break;
        }
    }

    determineActiveTabAtPageload();

    $scope.currentPage = {};
    $scope.currentPage.route = 0;
    $scope.currentPage.traj = {};
    $scope.currentPage.traj.mine = 0;
    $scope.currentPage.traj.iReq = 0;
    $scope.currentPage.traj.reqMyRoutes = 0;

    $scope.itemsPerPage = itemsPerPage;
    $scope.numberOfPages = function (data) {
        if (data) {
            result = Math.ceil(data.length / $scope.itemsPerPage);
            return (result > 0) ? result : 1;
        } else {
            return -1;
        }
    }

}]);
/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: My profile
var gender = null;
var avatarUrl = null ;
carpoolingApp.controllerProvider.register('myProfileCtrl', ['$scope', '$http', '$location', 'SharedProperties', "$authChecker", function ($scope, $http, $location, SharedProperties, $authChecker) {
    deleteActiveClass();
    $('#MyProfileTab').addClass('active');

    //$authChecker.checkAuthorization();


    $scope.routes=[];

    //$scope.carPicture = 'http://localhost8080:BackEnd/carImages/';
    var counter = 0;
    var username = null;

    var defaultActive = function(){
        $('#routeTab').removeClass('activeTab');
        $('#trajectTab').removeClass('activeTab');
        $('#carTab').removeClass('activeTab');
    }

    $http({
        method: 'GET',
        url: rootUrl + "/authorized/myprofile/",
        headers: {'Content-Type': "text/plain; charset=utf-8"}
    }).success(function (response) {
            obj = response;
            if (obj.hasOwnProperty("error")) {
                if (obj["error"] == "AuthorizationNeeded") {
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
                $scope.username = obj["username"]
                if (obj["smoker"]) {
                    $scope.smoker = "Roker"
                } else {
                    $scope.smoker = "Geen roker"
                }
            }
        });


    $scope.clickCar = function (car) {
        window.location.href = "http://localhost:8080/frontend/app/index.html#/myProfile/changeRemoveCar/" + car.carId + "";
    };

    $scope.tabRoutesClick = function () {
        defaultActive();
        $('#routeTab').addClass('activeTab');

        $scope.hideCars = true;
        $scope.hideRoutes = false;
        $scope.hideTrajects = true;
        $scope.addText = "Voeg route toe";
    };
    $scope.tabCarsClick = function () {
        defaultActive();
        $('#carTab').addClass('activeTab');
        $scope.hideCars = false;
        $scope.hideRoutes = true;
        $scope.hideTrajects = true;
        $scope.addText = "Voeg auto toe";
    };
    $scope.tabTrajectsClick = function () {
        defaultActive();
        $('#trajectTab').addClass('activeTab');
        loadTrajects();
        $scope.hideCars = true;
        $scope.hideRoutes = true;
        $scope.hideTrajects = false;
        $scope.addText = "Voeg traject toe";
    };

    $scope.goRoute = function(id,day) { $location.path("/route/"+id).hash(day); };
    $scope.goAddRoute = function() { $location.path("/addRoute"); };
    $scope.goChangeRoute = function(id) { $location.path("/changeRoute/"+id); };


    $scope.formatDate = function(date) {
        if (date == undefined) return "";
        return date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getFullYear();
    };
    $scope.formatDateString = function(dateString) {
        var date = new Date(dateString);
        return $scope.formatDate(date);
    };
    $scope.booleanL10n = function(b) { return b ? "Ja" : "Nee"; };

    $scope.goToProfile=function(userId) {
        $location.path("/profile/"+userId);
    };

    function loadRoutes() {
        $http.get(rootUrl+"/authorized/route/mine").success(function (data) {
            $scope.routes=data;
        });
    }

    function loadTrajects() {
        $scope.trajects={
            mine:[],
            iRequested:[],
            requestedOnMyRoutes:[],
            mineLoaded:false,
            iRequestedLoaded:false,
            requestedOnMyRoutesLoaded:false,
            mineEmpty:undefined,
            iRequestedEmpty:undefined,
            requestedOnMyRoutesEmpty:undefined
        };
        //id, pickup, dropoff, weekday, route (id, chauffeur (id, name, avatarURL))
        $http.get(rootUrl+"/authorized/traject/mine").success(function (data) {
            if (data.hasOwnProperty("error") && data.error == "AuthorizationNeeded") {
                //TODO
                return;
            }
            $scope.trajects.mine=data;
            $scope.trajects.mineLoaded = true;
            $scope.trajects.mineEmpty =  ($scope.trajects.mine.length == 0);
        });
        $http.get(rootUrl+"/authorized/traject/i-requested").success(function (data) {
            if (data.hasOwnProperty("error") && data.error == "AuthorizationNeeded") {
                //TODO
                return;
            }
            $scope.trajects.iRequested=data;
            $scope.trajects.iRequestedLoaded = true;
            $scope.trajects.iRequestedEmpty =  ($scope.trajects.iRequested.length == 0);
        });
        $http.get(rootUrl+"/authorized/traject/requested-on-my-routes").success(function (data) {
            if (data.hasOwnProperty("error") && data.error == "AuthorizationNeeded") {
                //TODO
                return;
            }
            $scope.trajects.requestedOnMyRoutes=data;
            $scope.trajects.requestedOnMyRoutesLoaded = true;
            $scope.trajects.requestedOnMyRoutesEmpty =  ($scope.trajects.requestedOnMyRoutes.length == 0);
        });
    }

    $scope.getAvatarUrl=function(){
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

    $scope.acceptTraject=function(id) {
        $http.post(rootUrl+"/authorized/traject/"+id+"/accept").success(function(data) {
            if (data.hasOwnProperty("status") && data.status == "ok") {
                loadTrajects();
            } else if (data.hasOwnProperty("error")) {
                if (data.error == "Unauthorized") {
                    $scope.trajects.error.unauthorized=true;
                }
            }
        });
    };

    $scope.rejectTraject=function(id) {
        $http.post(rootUrl+"/authorized/traject/"+id+"/reject").success(function(data) {
            if (data.hasOwnProperty("status") && data.status == "ok") {
                loadTrajects();
            } else if (data.hasOwnProperty("error")) {
                if (data.error == "Unauthorized") {
                    $scope.trajects.error.unauthorized=true;
                }
            }
        });
    };

    function determineActiveTabAtPageload() {
        switch ($location.hash()) {
            case "cars":
            default:
                $scope.tabCarsClick();
                break;
            case "trajects":
                $scope.tabTrajectsClick();
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
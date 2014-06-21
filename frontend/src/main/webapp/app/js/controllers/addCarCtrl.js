carpoolingApp.controllerProvider.register('addCarCtrl', ['$scope', '$http', '$location', '$fileUpload', function ($scope, $http, $location, $fileUpload) {

    deleteActiveClass();
    $('#MyProfileTab').addClass('active');

    var rootUrl = "http://localhost:8080/BackEnd/";
    var insertedCar = 0;

    var stateId=2;

    /*if (readCookie("Token") == null) {
        $location.path("/login");
    }*/

    $scope.cdSubmit = function() {
        $scope.canStartCar=true;
        $scope.cdForm.$error.required = !!$scope.cdForm.brand.$error.required || !!$scope.cdForm.type.$error.required
            || !!$scope.cdForm.fueltype.$error.required || !!$scope.cdForm.consumption.$error.required;
        var data = {
            brand: $scope.cdBrand, type: $scope.cdType, fueltype: $scope.cdFueltype, consumption: $scope.cdConsumption
        };
        $scope.cdInProgress=true;
        $http.post(rootUrl + "/authorized/user/car/add/", JSON.stringify(data)).success(function (response) {
            $scope.cdInProgress=false;
            if (response.hasOwnProperty("inserted")) {
                insertedCar = response.inserted;
                openNextPart();
            } else if (response.hasOwnProperty("error")) {
                if (response.error == "MissingDataException") { $scope.cdForm.$error.required = true; }
                else { $scope.cdForm.$error.unknown = true; }
            }
        });
    };

    $scope.ciUpload = function() {
        $scope.ciInProgress=true;
        $fileUpload.upload($scope.ciFile,rootUrl+"/authorized/user/car/" + insertedCar + "/uploadphoto").success(function(response) {
            $scope.ciInProgress=false;
            if (response.hasOwnProperty("error")) {
                if (response.error == "CarNotFound") { $scope.ciForm.$error.carnotfound = true; }
                else if (response.error == "CarNotYours") { $scope.ciForm.$error.carnotyours = true; }
                else if (response.error == "ImageError") { $scope.ciForm.file.$error.uploading = true;}
                else { $scope.ciForm.$error.unknown = true;}
            } else if (response.hasOwnProperty("url")) {
                $scope.ciURL = response.url;
                $scope.ciReady = true;
            }
        }).error(function() {
                $scope.ciInProgress=false;
                $scope.ciForm.$error.unknown = true;
            });
    };

    $scope.ciContinue = function() { goMyProfile(); };

    $scope.ciCancel = function() {
        $scope.ciInProgress=true;
        $http.post(rootUrl+"/authorized/user/car/" + insertedCar + "/deletephoto/", "").success(function (response) {
            $scope.ciInProgress=false;
            if (response.hasOwnProperty("status")) {
                $scope.ciURL="";
                $scope.ciReady=false;
            } else { $scope.ciForm.$error.removingUnknown = true; }
        });
    };

    $scope.getCiUrl = function() {
        if (!$scope.ciReady) {return ""; }
        return "http://localhost:8080/BackEnd/carImages/"+($scope.ciURL);
    };

    $scope.getNgShow = function(id) {
        if ($scope.canStartCar==false) { return false; }
        return id == stateId;
    };

    function openNextPart() {
        stateId++;
    }

    function goMyProfile() {
        $location.path("/myProfile");
        $("#cssmenu").show();
    }
}]);
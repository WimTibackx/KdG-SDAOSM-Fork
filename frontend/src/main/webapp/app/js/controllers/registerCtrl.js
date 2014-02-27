// CONTROLLER: Register
// Uses: service $fileUpload (services.js)
// Uses: directive fileModel (directives.js)
carpoolingControllers.controller('registerCtrl', ['$scope', '$http', '$location', '$fileUpload', function ($scope, $http, $location, $fileUpload) {
    console.log("hey test register ctrl");
    var rootUrl = "http://localhost:8080/BackEnd";
    var stateId=0;

    var insertedCar = 0;
    var accounttype = undefined;

    $scope.udSubmit = function() {
        $scope.udForm.$error.required = !!$scope.udForm.username.$error.required || !!$scope.udForm.password.$error.required
            || !!$scope.udForm.name.$error.required || !!$scope.udForm.gender.$error.required
            || !!$scope.udForm.smoker.$error.required || !!$scope.udForm.dob.$error.required
            || !!$scope.udForm.accounttype.$error.required;
        accounttype = $scope.udAccounttype;
        var data = {
            username: $scope.udUsername, password: $scope.udPassword, name: $scope.udName,
            smoker: $scope.udSmoker, gender: $scope.udGender, dateofbirth: $scope.udDoB.toISOString().split("T")[0]
        };
        $scope.udInProgress=true;
        $http.post(rootUrl+"/register/", JSON.stringify(data)).success(function (response) {
            $scope.udInProgress=false;
            if (response.hasOwnProperty("result")) {
                openNextPart();
            } else if (response.hasOwnProperty("error")) {
                if (response.error == "ParseError") { $scope.udForm.$error.parse = true; }
                else if (response.error == "PasswordFormatException") { $scope.udForm.password.$error.pattern = true; }
                else if (response.error == "UsernameFormatException") { $scope.udForm.username.$error.pattern = true; }
                else if (response.error == "UsernameExistsException") { $scope.udForm.username.$error.exists = true; }
                else if (response.error == "MissingDataException") { $scope.udForm.$error.required = true; }
                else { $scope.udForm.$error.unknown = true; }
            }
        });
    };

    $scope.uiUpload = function() {
        $scope.uiInProgress=true;
        $fileUpload.upload($scope.uiFile,rootUrl+"/authorized/user/uploadphoto").success(function(response) {
            $scope.uiInProgress=false;
            if (response.hasOwnProperty("error")) {
                if (response.error == "ImageError") { $scope.uiForm.$error.image = true; }
                else { $scope.uiForm.$error.unknown = true; }
            } else if (response.hasOwnProperty("url")) {
                $scope.uiURL = response.url;
                $scope.uiReady = true;
            }
        }).error(function(response) { $scope.uiForm.$error.unknown = true; });
    };

    $scope.uiContinue = function() { $scope.isDriver() ? openNextPart() : goMyProfile(); };

    $scope.uiCancel = function() {
        $scope.uiInProgress=true;
        $http.post(rootUrl+"/authorized/user/deletephoto/", "").success(function (response) {
           $scope.uiInProgress=false;
           if (response.hasOwnProperty("status")) {
               $scope.uiURL="";
               $scope.uiReady=false;
           } else { $scope.uiForm.$error.removeimage = true; }
        });
    };

    $scope.cdSubmit = function() {
        $scope.cdForm.$error.required = !!$scope.cdForm.brand.$error.required || !!$scope.cdForm.type.$error.required
            || !!$scope.cdForm.fueltype.$error.required || !!$scope.cdForm.consumption.$error.required;
        console.log("is invalid: "+$scope.cdForm.$invalid);
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
        });
    };

    $scope.ciContinue = function() { goMyProfile(); };

    $scope.ciCancel = function() {
        $scope.ciInProgress=true;
        $http.post(rootUrl+"/authorized/user/car/" + insertedCar + "deletephoto/", "").success(function (response) {
            $scope.ciInProgress=false;
            if (response.hasOwnProperty("status")) {
                $scope.ciURL="";
                $scope.ciReady=false;
            } else { $scope.ciForm.$error.removingUnknown = true; }
        });
    };

    $scope.getNgShow = function(id) { return id == stateId; };

    $scope.isDriver = function() { return accounttype=="driver"; };

    $scope.getUiUrl = function() {
        if (!$scope.uiReady) {return ""; }
        return "http://localhost:8080/BackEnd/userImages/"+($scope.uiURL);
    };

    $scope.getCiUrl = function() {
        if (!$scope.ciReady) {return ""; }
        return "http://localhost:8080/BackEnd/carImages/"+($scope.ciURL);
    };

    function openNextPart() {
        stateId++;
    }

    function goMyProfile() {
        $location.path("/myProfile");
        $("#cssmenu").show();
    }
}]);
// CONTROLLER: Register
// Uses: service $fileUpload (services.js)
// Uses: directive fileModel (directives.js)
carpoolingControllers.controller('registerCtrl', ['$scope', '$http', '$location', '$fileUpload', function ($scope, $http, $location, $fileUpload) {
    console.log("hey test register ctrl");
    var rootUrl = "http://localhost:8080/BackEnd";
    var states=["#registerform","#userimageform","#driver-cardata","#carimageform"];
    var stateId=0;
    var state=states[stateId];

    var insertedCar = 0;
    var accounttype = undefined;

    $scope.udSubmitted=false;
    $scope.uiSubmitted=false;
    $scope.cdSubmitted=false;
    $scope.ciSubmitted=false;

    $scope.udHasAnyErrors=false;
    $scope.uiHasAnyErrors=false;

    $scope.udSubmit = function() {
        $scope.udSubmitted=true;
        $scope.udCanError();
        if ($scope.udHasAnyErrors) { return; }
        accounttype = $scope.udAccounttype;
        var data = {
            username: $scope.udUsername, password: $scope.udPassword, name: $scope.udName,
            smoker: $scope.udSmoker, gender: $scope.udGender, dateofbirth: $scope.udDoB.toISOString().split("T")[0]
        };
        $http.post(rootUrl+"/register/", JSON.stringify(data)).success(function (response) {
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
        $scope.uiSubmitted=true;
        $fileUpload.upload($scope.uiFile,rootUrl+"/authorized/user/uploadphoto").success(function(response) {
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
        $scope.uiSubmitted=false;
        $http.post(rootUrl+"/authorized/user/deletephoto/", "").success(function (response) {
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
        $scope.cdSubmitted=true;
        var data = {
            brand: $scope.cdBrand, type: $scope.cdType, fueltype: $scope.cdFueltype, consumption: $scope.cdConsumption
        };
        $http.post(rootUrl + "/authorized/user/car/add/", JSON.stringify(data)).success(function (response) {
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
        $scope.ciSubmitted=true;
        $fileUpload.upload($scope.ciFile,rootUrl+"authorized/user/car/" + insertedCar + "/uploadphoto").success(function(response) {
            if (response.hasOwnProperty("error")) {
                if (response.error == "CarNotFound") {
                    showError("There was a problem with the car");
                } else if (response.error == "CarNotYours") {
                    showError("This car isn't yours!");
                } else if (response.error == "ImageError") {
                    showError("An error occured while uploading the images");
                } else {
                    showError("An unknown error occured.");
                }
            } else if (response.hasOwnProperty("url")) {
                $scope.ciURL = response.url;
                $scope.ciReady = true;
            }
        });
    };

    $scope.ciContinue = function() { goMyProfile(); };

    $scope.ciCancel = function() {
        $scope.ciSubmitted=false;
        $http.post(rootUrl+"/authorized/user/car/" + insertedCar + "deletephoto/", "").success(function (response) {
            if (response.hasOwnProperty("status")) {
                $scope.ciURL="";
                $scope.ciReady=false;
            } else {
                showError("Something went wrong while removing your image");
            }
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

    $scope.udCanError = function() {
        /*$scope.udHasAnyErrors = $scope.udSubmitted && ($scope.udForm.username.$invalid || $scope.udForm.password.$invalid
            || $scope.udForm.name.$invalid || $scope.udForm.gender.$invalid || $scope.udForm.smoker.$invalid || $scope.udForm.dob.$invalid
            || $scope.udForm.accounttype.$invalid);*/
        $scope.udForm.$error.required = !!$scope.udForm.username.$error.required || !!$scope.udForm.password.$error.required
            || !!$scope.udForm.name.$error.required || !!$scope.udForm.gender.$error.required
            || !!$scope.udForm.smoker.$error.required || !!$scope.udForm.dob.$error.required
            || !!$scope.udForm.accounttype.$error.required;
    };

    function showError(errorMsg) {
        $(state + " #error").text(errorMsg).show();
        $("#cssmenu").hide();
    }

    function openNextPart() {
        stateId++;
        state = states[stateId];
    }

    function goMyProfile() {
        $location.path("/myProfile");
        $("#cssmenu").show();
    }
}]);
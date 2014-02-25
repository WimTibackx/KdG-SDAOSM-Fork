// CONTROLLER: Register
// Uses: service $fileUpload (services.js)
// Uses: directive fileModel (directives.js)
carpoolingControllers.controller('registerCtrl', ['$scope', '$http', '$location', '$fileUpload', function ($scope, $http, $location, $fileUpload) {
    console.log("hey test register ctrl");
    $("#datepicker").datepicker();

    var rootUrl = "http://localhost:8080/BackEnd/";
    var states=["#registerform","#userimageform","#driver-cardata","#carimageform"];
    var stateId=0;
    var state=states[stateId];

    var insertedCar = 0;
    var accounttype = undefined;

    $scope.udSubmit = function() {
        accounttype = $scope.udAccounttype;
        var data = {
            username: $scope.udUsername, password: $scope.udPassword, name: $scope.udName,
            smoker: $scope.udSmoker, gender: $scope.udGender, dateofbirth: $('#datepicker').val()
        };
        $http.post(rootUrl+"register/", JSON.stringify(data)).success(function (response) {
            if (response.hasOwnProperty("result")) {
                openNextPart();
            } else if (response.hasOwnProperty("error")) {
                if (response.error == "RegisterWrong") {
                    showError("One of the fields is wrong");
                } else if (response.error == "ParseError") {
                    showError("There is a problem with our server, please try again later");
                } else if (response.error == "PasswordFormatException") {
                    showError("The password doesn't adhere to the format [1 uppercase, 1 lowercase, 1 digit, no whitespaces, 7-30 length].");
                } else if (response.error == "UsernameFormatException") {
                    showError("Please enter a valid e-mailaddress");
                } else if (response.error == "UsernameExistsException") {
                    showError("This e-mailaddress is already in use.");
                } else {
                    showError("An unknown error occured.");
                }
            }
        });
    };

    $scope.uiUpload = function() {
        $fileUpload.upload($scope.uiFile,rootUrl+"authorized/user/uploadphoto").success(function(response) {
            if (response.hasOwnProperty("error")) {
                if (response.error == "ImageError") {
                    showError("An error occured while uploading the images");
                } else {
                    showError("An unknown error occured.");
                }
            } else if (response.hasOwnProperty("url")) {
                $scope.uiURL = response.url;
                $scope.uiReady = true;
            }
        });
    };

    $scope.uiContinue = function() { $scope.isDriver() ? openNextPart() : goMyProfile(); };

    $scope.uiCancel = function() {
        $http.post(rootUrl+"/authorized/user/deletephoto/", "").success(function (response) {
           if (response.hasOwnProperty("status")) {
               $scope.uiURL="";
               $scope.uiReady=false;
           } else {
               showError("Something went wrong while removing your image");
           }
        });
    };

    $scope.cdSubmit = function() {
        var data = {
            brand: $scope.cdBrand, type: $scope.cdType, fueltype: $scope.cdFueltype, consumption: $scope.cdConsumption
        };
        $http.post(rootUrl + "authorized/user/car/add/", JSON.stringify(data)).success(function (response) {
            if (response.hasOwnProperty("inserted")) {
                insertedCar = response.inserted;
                openNextPart();
            } else if (response.hasOwnProperty("error")) {
                showError(response.error);
            }
        });
    };

    $scope.ciUpload = function() {
        $fileUpload.upload($scope.uiFile,rootUrl+"authorized/user/car/" + insertedCar + "/uploadphoto").success(function(response) {
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
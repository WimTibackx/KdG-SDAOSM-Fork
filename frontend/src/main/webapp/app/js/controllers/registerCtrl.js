// CONTROLLER: Register
// Uses: service $fileUpload (services.js)
// Uses: directive fileModel (directives.js)
carpoolingApp.controllerProvider.register('registerCtrl', ['$scope', '$http', '$location', '$fileUpload', function ($scope, $http, $location, $fileUpload) {
    console.log("hey test register ctrl");

    var rootUrl = "http://localhost:8080/BackEnd/";
    var stateId=0;

    //var insertedCar = 0;
    var accounttype = undefined;
    $scope.canStartCar=false;

    $scope.udSubmit = function() {
        $scope.udForm.$error.required = !!$scope.udForm.username.$error.required || !!$scope.udForm.password.$error.required
            || !!$scope.udForm.name.$error.required || !!$scope.udForm.gender.$error.required
            || !!$scope.udForm.smoker.$error.required || !!$scope.udForm.dob.$error.required
            || !!$scope.udForm.accounttype.$error.required;
        accounttype = $scope.udAccounttype;
        var data = {
            username: $scope.udUsername, password: $scope.udPassword, name: $scope.udName,
            smoker: $scope.udSmoker, gender: $scope.udGender, dateofbirth: ($scope.udDoB == undefined ? "" : $scope.udDoB.toISOString().split("T")[0])
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
        }).error(function() {
            $scope.udInProgress=false;
            $scope.udForm.$error.unknown = true;
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
        }).error(function(response) {
            $scope.uiInProgress = false;
            $scope.uiForm.$error.unknown = true;
        });
    };

    $scope.uiContinue = function() {
        $scope.canStartCar=$scope.isDriver();
        $scope.isDriver() ? openNextPart() : goMyProfile();
    };

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

    $scope.getNgShow = function(id) { return id == stateId; };

    $scope.isDriver = function() { return accounttype=="driver"; };

    $scope.getUiUrl = function() {
        if (!$scope.uiReady) {return ""; }
        return "http://localhost:8080/BackEnd/userImages/"+($scope.uiURL);
    };

    function openNextPart() {
        stateId++;
    }

    function goMyProfile() {
        $location.path("/myProfile");
        $("#cssmenu").show();
    }
}]);
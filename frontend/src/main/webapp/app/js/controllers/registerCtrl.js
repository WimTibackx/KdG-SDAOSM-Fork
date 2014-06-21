// CONTROLLER: Register
// Uses: service $fileUpload (services.js)
// Uses: directive fileModel (directives.js)
carpoolingApp.controllerProvider.register('registerCtrl', ['$scope', '$http', '$location', '$fileUpload', 'cpa.svc.api.v1', function ($scope, $http, $location, $fileUpload, cpa_api) {
	var accounttype = undefined;
	
	$scope.ud = { 
		username: null, password: null, name: null, gender: null, smoker: null, dateOfBirth: null, accountType: null,
		errors: { required: false, parse: false, unknown: false },
		inProgress: false,
		usernamePattern: "/^[_a-z0-9-A-Z-]+(\.[_a-z0-9-A-Z-]+)*@[a-z0-9-A-Z-]+(\.[a-z0-9-A-Z-]+)*(\.[a-zA-Z]{2,4})$/",
		passwordPattern: "/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{7,30}$/"
	};
	
	$scope.ud.submit = function() {
		udRequiredCheck();
		accounttype = $scope.ud.accountType.$modelValue; //TODO: Recheck
		var username = $scope.ud.username.$modelValue,
		password = $scope.ud.password.$modelValue,
		name = $scope.ud.name.$modelValue,
		gender = $scope.ud.gender.$modelValue,
		smoker = $scope.ud.smoker.$modelValue,
		dateOfBirth = ($scope.ud.dateOfBirth.$modelValue == undefined ? "" : $scope.ud.dateOfBirth.$modelValue.toISOString().split("T")[0]),
		
		cbAll = function() { $scope.ud.inProgress = false; },
		cbSuccess = function() { openNextPart(); },
		cbParseError = function() { $scope.ud.errors.parse = true; },
		cbPassFormat = function() { $scope.ud.password.$errors.pattern = true; },
		cbUserFormat = function() { $scope.ud.username.$errors.pattern = true; },
		cbUserExists = function() { $scope.ud.username.$errors.exists = true; },
		cbMissingData = function() { $scope.ud.errors.required = true; },
		cbUnknownError = function() { $scope.ud.errors.unknown = true; };
		
		$scope.ud.inProgress = true;
		cpa_api.register(username, password, name, gender, smoker, dateOfBirth, cbAll, cbSuccess, cbParseError, cbPassFormat, cbUserFormat, cbUserExists, cbMissingData, cbUnknownError);
	};
	
	function udRequiredCheck() {
		var x = $scope.ud;
		$scope.ud.errors.required = !!x.username.$error.required || !!x.password.$error.required 
			|| !!x.name.$error.required || !!x.gender.$error.required || !!x.smoker.$error.required 
			|| !!x.dateOfBirth.$error.required || !!x.accountType.$error.required;
	}
	
    var rootUrl = "http://localhost:8080/BackEnd/";
    var stateId=0;

    //var insertedCar = 0;
    
    $scope.canStartCar=false;

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
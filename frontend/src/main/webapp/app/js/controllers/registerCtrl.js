carpoolingApp.controllerProvider.register('registerCtrl', ['$scope', '$http', '$location', '$fileUpload', 'cpa.svc.api.v1', function ($scope, $http, $location, $fileUpload, cpa_api) {
	$scope.ud = { 
		model: {},
		errors: { required: false, parse: false, unknown: false, passFormat: false, userFormat: false, userExists: false },
		inProgress: false,
		usernamePattern: "",
		passwordPattern: ""
	};
	
	$scope.ud.submit = function() {
		console.log("submitted");
		udRequiredCheck();
		var username = $scope.ud.model.username,
		password = $scope.ud.model.password,
		name = $scope.ud.model.name,
		gender = $scope.ud.model.gender,
		smoker = $scope.ud.model.smoker,
		dateOfBirth = ($scope.ud.model.dateOfBirth == undefined ? "" : $scope.ud.model.dateOfBirth.toISOString().split("T")[0]),
		
		cbAll = function() { $scope.ud.inProgress = false; },
		cbSuccess = function() { console.log("to myprofile"); goMyProfile(); },
		cbParseError = function() { $scope.ud.errors.parse = true; },
		cbPassFormat = function() { $scope.ud.errors.passFormat = true; },
		cbUserFormat = function() { $scope.ud.errors.userFormat = true; },
		cbUserExists = function() { $scope.ud.errors.userExists = true; },
		cbMissingData = function() { $scope.ud.errors.required = true; },
		cbUnknownError = function() { $scope.ud.errors.unknown = true; };
		
		$scope.ud.inProgress = true;
		cpa_api.register(username, password, name, gender, smoker, dateOfBirth, cbAll, cbSuccess, cbParseError, cbPassFormat, cbUserFormat, cbUserExists, cbMissingData, cbUnknownError);
	};
	
	function udRequiredCheck() {
		var x = $scope.udForm;
		$scope.ud.errors.required = !!x.username.$error.required || !!x.password.$error.required 
			|| !!x.name.$error.required || !!x.gender.$error.required || !!x.smoker.$error.required 
			|| !!x.dateOfBirth.$error.required;
	}

    function goMyProfile() {
        $location.path("/myProfile");
        $("#cssmenu").show();
    }
}]);
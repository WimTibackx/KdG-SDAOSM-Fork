/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: Login
carpoolingApp.controllerProvider.register('loginCtrl', ['$scope', '$http', '$location', 'SharedProperties', 'cpa.svc.api.v1', function ($scope, $http, $location, SharedProperties, cpa_api) {
	$scope.form = { username: "", password: "", error: ""};
	
	$scope.doLogin = function() {
		var username = $scope.form.username,
		password = $scope.form.password,
		cbAll = function() {},
		cbSuccess = function() {
			$location.path("/myProfile");
            $("#cssmenu").show();
            $scope.form.error = "";
		},
		cbComboWrong = function() {
			$scope.form.error = "Combination username/password is wrong";
            $("#cssmenu").hide();
		},
		cbParseError = function() {
			$scope.form.error = "There is a problem with our server, please try again later";
            $("#cssmenu").hide();
		};
		cpa_api.login(username, password, cbAll, cbSuccess, cbComboWrong, cbParseError);
	};
	
	$scope.form.hasError = function() { return $scope.form.error != ""; }
	
	function initError() {
		if (SharedProperties.getProperty() != null) {
			$scope.form.error = SharedProperties.getProperty();
	        SharedProperties.setProperty(null);
	    }
	}
    
	initError();
    if (readCookie("Token") != null) {
    	$location.path("/myProfile");
    }

    $("#cssmenu").hide();
}]);
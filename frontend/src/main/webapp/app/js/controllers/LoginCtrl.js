angular.module("cpa.ctrl").controller('LoginCtrl', ['$scope', '$state', 'cpa.svc.api.v1', function ($scope, $state, cpa_api) {
	$scope.form = { username: "", password: "", errors: {} };
	
	$scope.doLogin = function() {
		resetErrors();
		var username = $scope.form.username,
		password = $scope.form.password,
		cbAll = function() {},
		cbSuccess = function() { $state.go("myProfile"); },
		cbComboWrong = function() { $scope.form.errors.comboWrong = true; },
		cbParseError = function() { $scope.form.errors.parseError = true; };
		cbUnknownError = function() { $scope.form.errors.unknownError = true; };
		cpa_api.login(username, password, cbAll, cbSuccess, cbComboWrong, cbParseError, cbUnknownError);
	};
	
	$scope.form.hasError = function() { return $scope.form.error != ""; }
	
	function resetErrors() {
		$scope.form.errors = { comboWrong: false, parseError: false, unknownError: false };
	}
	function initError() {
		//TODO: Fix this once we have a central message-infrastructure
		if (SharedProperties.getProperty() != null) {
			$scope.form.error = SharedProperties.getProperty();
	        SharedProperties.setProperty(null);
	    }
	}
    
	//initError();
	resetErrors();
}]);
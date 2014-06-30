angular.module("cpa.ctrl").controller('ChangePasswordCtrl', ['$scope', 'cpa.svc.api.v1', function ($scope, cpa_api) {
	$scope.messages = {
		Success: false,
		reset: function() {
			$scope.messages.Success = false;
		}
	};
	$scope.errors = {
		OldPasswordWrong: false,
		Format: false,
		Unavailable: false,
		NewNotEqual: false,
		reset: function() {
			$scope.errors.OldPasswordWrong = false;
			$scope.errors.Format = false;
			$scope.errors.Unavailable = false;
			$scope.errors.NewNotEqual = false;
		}
	};
	
    $scope.PasswordSubmit = function () {
    	$scope.messages.reset();
    	$scope.errors.reset();
    	
    	if ($scope.newpwd1 != $scope.newpwd2) {
    		$scope.errors.NewNotEqual = true;
    		return;
    	}
    	
    	var oldPassword = $scope.oldpwd,
    		newPassword = $scope.newpwd1,
    		
    		cbAll = function() {},
    		cbSuccess = function() { $scope.messages.Success = true; },
    		cbOldPasswordWrong = function() { $scope.errors.OldPasswordWrong = true; },
    		cbFormat = function() { $scope.errors.Format = true; },
    		cbUnknown = function() { $scope.errors.Unavailable = true; };
    	
    	cpa_api.changePassword(oldPassword, newPassword, cbAll, cbSuccess, cbOldPasswordWrong, cbFormat, cbUnknown);
    }
}]);
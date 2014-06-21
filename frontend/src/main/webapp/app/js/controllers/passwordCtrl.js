/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: Password
carpoolingApp.controllerProvider.register('passwordCtrl', ['$scope', '$http', '$location', 'cpa.svc.api.v1', function ($scope, $http, $location, cpa_api) {
    $scope.form = { username: "" };
    $scope.PWRFeedback = "";
    $scope.displayPWRFeedback = false;
    $scope.PWRClass = "";
    
    $scope.form.submit = function() {
    	var cbAll = function() { $scope.displayPWRFeedback = true; },
    	cbSuccess = function() {
    		$scope.PWRClass = 'success';
            $scope.PWRFeedback = 'New password sent to ' + $scope.form.username;
    	},
    	cbError = function(msg) {
    		$scope.PWRClass = 'error';
            $scope.PWRFeedback = msg;
    	};
    	cpa_api.resetPassword($scope.form.username, cbAll, cbSuccess, cbError);
    };
}]);
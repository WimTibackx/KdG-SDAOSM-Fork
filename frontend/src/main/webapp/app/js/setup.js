angular.module("cpa.svc", []);
angular.module("cpa.dve", ["cpa.dve"]);
angular.module("cpa.ctrl", ["cpa.svc"]);

angular.module("cpa.ctrl").controller("MyProfileCtrl", ['$scope', function($scope) {
	$scope.name = "Wim Tibackx";
}]);
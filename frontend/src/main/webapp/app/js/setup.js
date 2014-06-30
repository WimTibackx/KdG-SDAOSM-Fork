angular.module("cpa.svc", ["ngCookies"]);
angular.module("cpa.dve", ["cpa.svc"]);
angular.module("cpa.ctrl", ["cpa.svc"]);

angular.module("cpa.dve").directive("cpaMenuItem",['$state',function($state) {
	return {
		restrict: 'E',
		templateUrl: 'partials/dveMenuItem.html',
		scope: {
			state: "@",
			text: "@",
			linkId: "@",
			itemClass: "@"
		},
		replace: true,
		link: function($scope) {
			$scope.$state = $state;
		}
	}
}]);

angular.module("cpa.ctrl").controller("MyProfileCtrl", ['$scope', function($scope) {
	$scope.name = "Wim Tibackx";
}]);
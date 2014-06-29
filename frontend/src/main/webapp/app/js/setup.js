/*(function() {
	"use strict";*/
	
	angular.module("cpa.ctrl", []);
//})();

//(function() {
	
	angular.module("cpa.ctrl").controller("LoginCtrl", ['$scope', function($scope,$state) {
		console.log($state);
		$scope.foo = "foo!";
	}]);
	angular.module("cpa.ctrl").controller("RegisterCtrl", ['$scope', function($scope) {
		$scope.bar = "bar!";
	}]);
	angular.module("cpa.ctrl").controller("MyProfileCtrl", ['$scope', function($scope) {
		$scope.name = "Wim Tibackx";
	}]);
//});
function PageManagement(items) {
	this.allItems = items;
	
	this.current = 1;
	this.size = 9;
	this.max = Math.ceil(this.allItems.length / this.size);
	
	this.page = [];
	this.pageIsEmpty = false;
	this.canPrev = false;
	this.canNext = false;
	
	this._checkAbilities();
	this._fillPage();
}
PageManagement.prototype._fillPage = function() {
	var start = (this.current - 1) * this.size;
	this.page = this.allItems.slice(start, start + this.size);
	this.pageIsEmpty = (this.page.length == 0);
};
PageManagement.prototype._checkAbilities = function() {
	this.canPrev = (this.current > 1);
	this.canNext = (this.max > this.current);
};
PageManagement.prototype.goNext = function() {
	if (!this.canNext) { return; }
	this.current++;
	this._checkAbilities();
	this._fillPage();
};
PageManagement.prototype.goPrev = function() {
	if (!this.canPrev) { return; }
	this.current--;
	this._checkAbilities();
	this._fillPage();
};

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
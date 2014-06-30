angular.module("cpa.svc").factory('cpa.svc.auth.v1', ['cpa.svc.pageAccess.v1', '$cookies', '$state', function(cpa_pageAccess,$cookies,$state) {
	
	var hasCookie = undefined;
	var hasChanged = false;
	
	function booleaniseCookie() {
		hasCookie = ($cookies.Token != undefined);
	}
	
	function checkCookie() {
		hasChanged = false;
		var prevCookie = hasCookie;
		booleaniseCookie();
		if (prevCookie != hasCookie) {
			hasChanged = true;
			$state.go(hasCookie ? "myProfile" : "login");
			return;
		}
		return hasCookie;
	}
	
	var auth = {};
	auth.isGuest = function() { return !checkCookie(); };
	auth.isMember = function() { return checkCookie(); };
	auth.check = function() {
		if (cpa_pageAccess.isAny()) { return; }
		if (cpa_pageAccess.isGuest() && auth.isMember()) {
			if (hasChanged) { return; }
			$state.go("myProfile"); //TODO: Show error
			return;
		}
		if (cpa_pageAccess.isMember() && auth.isGuest()) {
			if (hasChanged) { return; }
			$state.go("login"); //TODO: Show error
			return;
		}
	};
	
	return auth;
}]);
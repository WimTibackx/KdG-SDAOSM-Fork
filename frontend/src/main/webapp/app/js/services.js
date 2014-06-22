var carpoolServices = angular.module('carpoolServices', ['ngCookies']);

carpoolServices.factory('SharedProperties', function () {
    var property = null;

    return{
        setProperty: function (value) {
            property = value;
        },
        getProperty: function () {
            return property;
        }
    }
});

carpoolServices.factory('cpa.svc.pageaccess.v1', function() {
	var current = undefined;
	var options = { GUEST: 'GUEST', MEMBER: 'MEMBER', ANY: 'ANY' };
	
	var external = {};
	external.set = function(option) {
		if (!options.hasOwnProperty(option)) {
			return undefined;
		}
		current = options[option];
	};
	external.setGuest = function() { external.set(options.GUEST); };
	external.setMember = function() { external.set(options.MEMBER); };
	external.setAny = function() { external.set(options.ANY); };
	
	external.get = function() { return current; };
	external.isGuest = function() { return external.get() == options.GUEST; };
	external.isMember = function() { return external.get() == options.MEMBER; };
	external.isAny = function() { return external.get() == options.ANY; };
	external.is = function(option) {
		if (!options.hasOwnProperty(option)) {
			return undefined;
		}
		return external.get() == options[option];
	}
	
	external.setAny();
	
	return external;
});

carpoolServices.service('$fileUpload', ['$http', function ($http) {
    this.upload = function (file, uploadUrl) {
        var fd = new FormData();
        fd.append('file', file);
        return $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        });
    }
}]);


carpoolServices.factory("$authChecker", ["$http", "$location", "SharedProperties", function($http, $location, SharedProperties) {
    function readCookie(name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
        }
        return null;
    }

    function toLogin() {
        SharedProperties.setProperty("Authorization is required.");
        $location.path("/login").hash("");
    }

    function toProfile() {
        $location.path("/myProfile").hash("");
    }

    return {
        checkAuthorization:function() {
            if (readCookie("Token") == null) toLogin();
            $http.get(rootUrl + "/authorized/checkAuthorization").success(function(data) {
                if (data.hasOwnProperty("error") && data.error == "AuthorizationNeeded") toLogin();
                else if (data.hasOwnProperty("status") && data.status == "ok") return true;
            });
        },
        checkNoAuthorization:function() {
            if (readCookie("Token") != null) toProfile();
            $http.get(rootUrl + "/authorized/checkAuthorization").success(function(data) {
                if (data.hasOwnProperty("error") && data.error == "AuthorizationNeeded") return true;
                else if (data.hasOwnProperty("status") && data.status == "ok") toProfile();
            });
        }
    }
}]);

carpoolServices.filter('startFrom', function() {
    return function(input, start) {
        if(!input) return null;
        start = +start;
        return input.slice(start);
    }
});

carpoolServices.factory('cpa.svc.auth.v1', ['cpa.svc.pageaccess.v1', '$cookies', '$location', function(cpa_pageaccess,$cookies,$location) {
	var auth = {};
	auth.isGuest = function() { return $cookies.Token == undefined; };
	auth.isMember = function() { return $cookies.Token != undefined; };
	auth.check = function() {
		if (cpa_pageaccess.isAny()) { return; }
		if (cpa_pageaccess.isGuest() && auth.isMember()) {
			$location.path('/myProfile'); //TODO: Show error
		}
		if (cpa_pageaccess.isMember() && auth.isGuest()) {
			$location.path("/login"); //TODO: Show error
		}
	};
	
	return auth;
}]);
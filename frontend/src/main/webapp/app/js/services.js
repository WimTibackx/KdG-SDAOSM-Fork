var carpoolServices = angular.module('carpoolServices', []);

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
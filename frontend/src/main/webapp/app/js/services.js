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

carpoolServices.factory('$api', ['$http', '$location', function ($http, $location) {
    var rootUrl = "http://localhost:8080/BackEnd";

    function errorHandling(data, status, headers, config) {
        statusCode = status;
        if(data.hasOwnProperty('error')) {
            if (data.error == "AuthorizationNeeded") statusCode = 401;
        }

        switch (statusCode) {
            case 404:
                console.log('Error: 404 Not Found');
                break;
            case 401:
                console.log('Error: 401 Not Authorized!')
                $location.path("/login");
                break;
            default:
                console.log('Error: status code ' + status);
                console.log('Technical mumbo-jumbo');
                console.log('Data:');
                console.log(data);
                console.log('Status');
                console.log(status);
                console.log('Headers');
                console.log(headers);
                console.log('Config');
                console.log(config);
                console.log('=====');
        }
    }

    return {
        get: function (endpoint, callback) {
            console.log("Get from " + endpoint);
            $http.get(rootUrl + endpoint)
                .success(function (data, status, headers, config) {
                    if (data.hasOwnProperty('error')) {
                        errorHandling(data, status, headers, config);
                    } else {
                        callback(status, data);
                    }
                })
                .error(errorHandling);
        },

        post: function (endpoint, data, callback) {
            console.log("Post to " + endpoint);
            $http.post(rootUrl + endpoint, data)
                .success(function (data, status, headers, config) {
                    if (data.hasOwnProperty('error')) {
                        errorHandling(data, status, headers, config);
                    } else {
                        callback(status, data);
                    }
                })
                .error(errorHandling);
        }
    }

}]);

carpoolServices.factory("$authChecker", ["$http", "$location", "SharedProperties", function($http, $location, SharedProperties) {
    console.log("$authorized initted");

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
        console.log("Redirecting user to login...");
        SharedProperties.setProperty("Authorization is required.");
        $location.path("/login").hash("");
    }

    function toProfile() {
        console.log("Redirecting user to profile...");
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
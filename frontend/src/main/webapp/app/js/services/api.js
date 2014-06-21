carpoolServices.factory('cpa.svc.api.v1', ['$http', function($http) {
	var rootUrl = "http://localhost:8080/BackEnd";
	
	var api = {};
	
	/*
	 * Login with a username and password
	 */
	api.login = function(username, password, cbAll, cbSuccess, cbComboWrong, cbParseError) {
		$http.post(rootUrl + "/login/", {username: username, password: password})
			.success(function (data, status, headers, config) {
				cbAll();
				if (data.hasOwnProperty("Token")) {
					cbSuccess();
				} else if (data.hasOwnProperty("error")) {
					if (data.error == "LoginComboWrong") {
						cbComboWrong();
					} else if (data.error == "ParseError") {
						cbParseError();
					}
					//TODO: Add unknown error callback
				}
			})
			.error(function (data, status, headers, config) {
				cbAll();
				//TODO: Add unknown error callback
			});
	};
	
	/*
	 * Register a new account
	 */
	api.register = function(username, password, name, smoker, gender, dateOfBirth, cbAll, cbSuccess, cbParseError, cbPassFormat, cbUserFormat, cbUserExists, cbMissingData, cbUnknownError) {
		var data = {username: username, password: password, name: name, smoker: smoker, gender: gender, dateofbirth: dateOfBirth};
		$http.post(rootUrl + "/register/", data)
			.success(function (data, status, headers, config) {
				cbAll();
				if (data.hasOwnProperty("result")) {
					cbSuccess();
				} else if (data.hasOwnProperty("error")) {
					switch (data.error) {
						case "ParseError": cbParseError(); break;
						case "PasswordFormatException": cbPassFormat(); break;
						case "UsernameFormatException": cbUserFormat(); break;
						case "UsernameExistsException": cbUserExists(); break;
						case "MissingDataException": cbMissingData(); break;
						default: cbUnknownError(); break;
					}
				}
			})
			.error(function (data, status, headers, config) {
				cbAll();
				cbUnknownError();
			});
	};
	
	/*
	 * Reset your password
	 */
	api.resetPassword = function(username, cbAll, cbSuccess, cbError) {
		$http.post(rootUrl + "/resetPassword/", {username: username})
			.success(function (data, status, headers, config) {
				cbAll();
				if (!result.hasOwnProperty("error")) {
					cbSuccess();
				} else {
					cbError(data.error);
				}
			})
			.error(function (data, status, headers, config) {
				cbAll();
				cbError(data);
			});
	};
	
	/*
	 * Check authorization
	 */
	api.checkAuthorization = function(cbAll, cbAuthorized, cbUnauthorized, cbUnknown) {
		$http.get(rootUrl + "/authorized/checkAuthorization")
			.success(function (data, status, headers, config) {
				cbAll();
				if (result.hasOwnProperty("status") && data.status == "ok") { cbAuthorized(); }
				else if (result.hasOwnProperty("error") && data.error == "AuthorizationNeeded") { cbUnauthorized(); }
				else { cbUnknown(data, status, headers, config); }
			})
			.error(function (data, status, headers, config) {
				cbAll();
				cbUnknown(data, status, headers, config);
			});
	};
	
	return api;
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
                break;
            case 401:
                $location.path("/login");
                break;
            default:
        }
    }

    return {
        get: function (endpoint, callback) {
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
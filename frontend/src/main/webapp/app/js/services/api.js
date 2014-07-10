angular.module("cpa.svc").factory('cpa.svc.api.v1', ['$http', function($http) {
	var rootUrl = "http://localhost:8080/BackEnd";
	
	var api = {};
	
	/*
	 * Login with a username and password
	 */
	api.login = function(username, password, cbAll, cbSuccess, cbComboWrong, cbParseError, cbUnknownError) {
		$http.post(rootUrl + "/login/", {username: username, password: password})
		.success(function (data, status, headers, config) {
			cbAll();
			if (data.hasOwnProperty("Token")) {
				cbSuccess();
			}
		})
		.error(function (data, status, headers, config) {
			cbAll();
			switch (status) {
				case 400:
					cbParseError();
				case 401:
					cbComboWrong();
				default:
					cbUnknownError();
			}
		});
	};
	
	/*
	 * Register a new account
	 */
	api.register = function(username, password, name, gender, smoker, dateOfBirth, cbAll, cbSuccess, cbParseError, cbPassFormat, cbUserFormat, cbUserExists, cbMissingData, cbUnknownError) {
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
		$http.get(rootUrl + "/authorized/checkAuthorization/")
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
	
	/*
	 * Logout
	 */
	api.logout = function(cbAll) {
		$http.get(rootUrl + "/authorized/logout/")
		.success(function (data, status, headers, config) {
			cbAll();
		});
	};
	
	/*
	 * Change password
	 */
	api.changePassword = function(oldPassword, newPassword, cbAll, cbSuccess, cbOldPasswordWrong, cbFormat, cbUnknown) {
		$http.post(rootUrl + "/authorized/changepassword", {oldpassword: oldPassword, newpassword: newPassword})
		.success(function (data, status, headers, config) {
			cbAll();
			if (data.hasOwnProperty("result")) {
				switch (data.result) {
					case "PasswordChanged":
						cbSuccess();
						return;
					case "OldPasswordWrong":
						cbOldPasswordWrong();
						return;
					case "NewPasswordFormatWrong":
						cbFormat();
						return;
					default:
						cbUnknown();
						return;
				}
			} else {
				cbUnknown();
				return;
			}
		}).error(function (data, status, headers, config) {
			cbAll();
			cbUnknown();
			return;
		});
	};
	
	api.notifyReadMsg = function (messageId, cbAll, cbSuccess, cbUnknown) {
		$http.post(rootUrl + "/authorized/textmessage/read", {messageId: messageId})
		.success(function (data, status, headers, config) {
			cbAll();
			cbSuccess();
		}).error(function (data, status, headers, config) {
			cbAll();
			cbUnknown();
		});
	};
	
	api.sendMsg = function(sender, receiver, msg, subject, cbAll, cbSuccess, cbUnknown) {
		var data = {
			senderUsername: sender,
			receiverUsername: receiver,
			messageBody: msg,
			messageSubject: subject
		};
		$http.post(rootUrl + "/authorized/textmessage/send", data)
		.success(function (data, status, headers, config) {
			cbAll();
			cbSuccess();
		}).error(function (data, status, headers, config) {
			cbAll();
			cbUnknown();
		});
	};
	
	api.getMsgs = function(cbAll, cbSuccess, cbError) {
		$http.get(rootUrl + "/authorized/textmessage/get")
		.success(function (data, status, headers, config) {
			cbAll();
			cbSuccess(data);
		}).error(function (data, status, headers, config) {
			cbAll();
			cbError();
		});
	};
	
	api.getMyProfile = function(cbAll, cbSuccess, cbError) {
		$http.get(rootUrl + "/authorized/myprofile")
		.success(function (data, status, headers, config) {
			cbAll();
			cbSuccess(data);
		}).error(function (data, status, headers, config) {
			cbAll();
			cbError();
		});
	};
	
	return api;
}]);
/*
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

}]);*/
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
        status = 401;
        switch (status) {
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
                    callback(status, data);
                })
                .error(errorHandling);
        },

        post: function (endpoint, data, callback) {
            console.log("Post to " + endpoint);
            $http.post(rootUrl + endpoint, data)
                .error(function(data, status, headers, config) {
                    callback(status, data);
                })
                .success(errorHandling);
        }
    }

}]);
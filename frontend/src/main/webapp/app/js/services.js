var carpoolServices = angular.module('carpoolServices', [])

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

carpoolServices.factory('$api', ['$http', function ($http) {
    var rootUrl = "http://localhost:8080/BackEnd";

    return {
        get: function (endpoint, callback) {
            console.log("Get from " + endpoint);
            $http.get(rootUrl + endpoint)
                .success(function (data, status, headers, config) {
                    callback(status, result);
                })
                .error(function (data, status, headers, config) {
                    if (status == 404) {
                        console.log('Error: 404 Not Found');
                    } else {
                        console.log('Error: status code ' + status)
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
                });
        },

        post: function (endpoint, data, callback) {
            console.log("Post to " + endpoint);
        }
    }

}]);
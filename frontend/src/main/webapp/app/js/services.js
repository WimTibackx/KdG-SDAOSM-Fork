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
    this.upload = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        return $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        });
    }
}]);

carpoolServices.factory('$api', function() {
    var rootUrl = "http://localhost:8080/BackEnd";

    return {
        get: function(endpoint, callback) {
            console.log("Get from " + endpoint);
            return $http.get()
        },

        post: function(endpoint, data, callback) {
            console.log("Post to " + endpoint);
        }
    }

});
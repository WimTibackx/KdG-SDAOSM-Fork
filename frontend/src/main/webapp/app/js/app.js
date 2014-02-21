var carpoolingApp = angular.module('carpoolingApp', ['ngRoute', 'carpoolingControllers'
]);

carpoolingApp.config(['$routeProvider',
    function ($routeProvider) {
        console.log("Check")
        console.log($routeProvider.path)
        $routeProvider.
            when('/login', {
                templateUrl: 'partials/login.html',
                controller: 'loginCtrl'
                //controller: 'registerCtrl'
            }).
            when('/myProfile', {
                templateUrl: 'partials/myProfile.html',
                controller: 'myProfileCtrl'
            }).
            when('/myProfile/changeRemoveCar/:carId', {
                templateUrl: 'partials/changeRemoveCar.html',
                controller: 'changeRemoveCarCtrl'
            }).
            when('/addRouteMap', {
                templateUrl: 'partials/addRouteMap.html'

            }).
            when('/addCar', {
                templateUrl: 'partials/addCar.html'

            }).
            when('/contact', {
                templateUrl: 'partials/contact.html'

            }).
            when('/about', {
                templateUrl: 'partials/about.html'

            }).
            when('/addCar', {
            	templateUrl: 'partials/addCar.html',
            	controller: 'addCarCtrl'
            }).
            otherwise({
                redirectTo: '/login'
            });
    }]);

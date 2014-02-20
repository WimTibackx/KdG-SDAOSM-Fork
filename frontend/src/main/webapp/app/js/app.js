var carpoolingApp = angular.module('carpoolingApp', ['ngRoute', 'carpoolingControllers'
]);

carpoolingApp.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
            when('/login', {
                templateUrl: 'partials/login.html',
                controller: 'loginCtrl',
                controller: 'registerCtrl'
            }).
            when('/myProfile', {
                templateUrl: 'partials/myProfile.html',
                controller: 'myProfileCtrl'
            }).
            when('/addRouteMap', {
                templateUrl: 'partials/addRouteMap.html'

            }).
            when('/contact', {
                templateUrl: 'partials/contact.html'

            }).
            when('/about', {
                templateUrl: 'partials/about.html'

            }).
            otherwise({
                redirectTo: '/login'
            });
    }]);
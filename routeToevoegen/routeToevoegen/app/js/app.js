var carpoolingApp = angular.module('carpoolingApp', ['ngRoute', 'carpoolingControllers'
]);

carpoolingApp.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.
            when('/myProfile', {
                templateUrl: 'partials/myProfile.html'

            }).
            when('/addRouteMap', {
                templateUrl: 'partials/addRouteMap.html'

            }).when('/register', {
                templateUrl: 'partials/register.html'

            }).
            when('/contact', {
                templateUrl: 'partials/contact.html'

            }).
            when('/about', {
                templateUrl: 'partials/about.html'

            }).
            when('/search', {
                templateUrl: 'partials/search.html'

            }).
            otherwise({
                redirectTo: '/myProfile'
            });
    }]);
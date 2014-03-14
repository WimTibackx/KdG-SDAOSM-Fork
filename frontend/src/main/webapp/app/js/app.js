var carpoolingApp = angular.module('carpoolingApp', ['ngRoute', 'carpoolingControllers', 'carpoolServices', 'carpoolDirectives']);

carpoolingApp.config(['$routeProvider', '$controllerProvider',
    function ($routeProvider, $controllerProvider) {
        carpoolingApp.controllerProvider = $controllerProvider;

        $routeProvider.
            when('/login', {
                templateUrl: 'partials/login.html'//,
                //controller: 'loginCtrl',
                //controller: 'registerCtrl'
            }).
            when('/myProfile', {
                templateUrl: 'partials/myProfile.html'//,
                //controller: 'myProfileCtrl'
            }).
            when('/myProfile/changeRemoveCar/:carId/', {
                templateUrl: 'partials/changeRemoveCar.html'
            }).
            when('/contact', {
                templateUrl: 'partials/contact.html'

            }).
            when('/about', {
                templateUrl: 'partials/about.html'

            }).
            when('/addCar', {
                templateUrl: 'partials/addCar.html'//,
                //controller: 'addCarCtrl'
            }).
            when('/addRoute', {
                templateUrl: 'partials/addRoute.html'//,
                //controller: 'addRouteCtrl'
            }).
            when('/changePassword', {
                templateUrl: 'partials/changePassword.html'//,
                //controller: 'changePasswordCtrl'
            }).
            when('/search', {
                templateUrl: 'partials/search.html'//,
                //controller: 'searchCtrl'
            }).
            when("/route/:routeId", {
                templateUrl: 'partials/viewRoute.html'
            }).
            when('/inbox', {
                templateUrl: 'partials/inbox.html'
            }).
            when('/changeRoute/:routeId', {
                templateUrl: 'partials/changeRoute.html'
            }).
            when('/profile/:userId',{
                templateUrl: 'partials/profile.html'
            }).
            otherwise({
                redirectTo: '/login'
            });
    }]);

// BEGIN GLOBAL VARIABLES
// Root URL for API calls
var rootUrl = "http://localhost:8080/BackEnd";

// Table paginating: items per page
var itemsPerPage = 1;

// END GLOBAL VARIABLES
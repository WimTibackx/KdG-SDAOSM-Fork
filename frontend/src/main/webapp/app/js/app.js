angular.module('cpa', ['ui.router', 'cpa.ctrl', 'cpa.svc', 'cpa.dve']);
angular.module('cpa').config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise("/login");
	
	$stateProvider.state("login", {
		url: "/login",
		templateUrl: "partials/login.html",
		controller: "LoginCtrl",
		data: { access: "GUEST" }
	}).state("register", {
		url: "/register",
		templateUrl: "partials/register.html",
		controller: "RegisterCtrl",
		data: { access: "GUEST" }
	}).state("resetPassword", {
		url: "/resetPassword",
		templateUrl: "partials/resetPassword.html",
		controller: "ResetPasswordCtrl",
		data: { access: "GUEST" }
	}).state("myProfile", {
		url: "/myProfile",
		template: "<h1>myProfile {{name}}</h1><div ui-view></div>",
		//templateUrl: "partials/myProfile.html",
		controller: "MyProfileCtrl",
		data: { access: "MEMBER" }
	}).state("myProfile.foo", {
		url: "/foo",
		template: "<h2>MyProfileFoo</h2>"
	}).state("myProfile.changePassword", {
		url: "/changePassword",
		templateUrl: "partials/changePassword.html",
		controller: "ChangePasswordCtrl"
	}).state("inbox", {
		url: "/inbox",
		templateUrl: "partials/inbox.html",
		controller: "InboxCtrl",
		data: { access: "MEMBER" }
	}).state("dummy", {
		url: "/dummy",
		template: "<h1>DUMMY</h1>",
		data: { access: "ANY" }
	});
}]).run(['$rootScope','$state','$stateParams','cpa.svc.pageAccess.v1', 'cpa.svc.auth.v1', function($rootScope, $state, $stateParams, cpa_pageAccess, cpa_auth) {
	$rootScope.$state = $state;
	$rootScope.$stateParams = $stateParams;
	$rootScope.auth = cpa_auth;
	$rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
		cpa_pageAccess.set(toState.data.access);
		cpa_auth.check();
	});
}]);

/*
var carpoolingApp = angular.module('carpoolingApp', ['ui.router', 'carpoolingControllers', 'carpoolServices', 'carpoolDirectives']);

carpoolingApp.config(['$routeProvider', '$controllerProvider', function ($routeProvider, $controllerProvider) {
    carpoolingApp.controllerProvider = $controllerProvider;

    $routeProvider
        .when('/login', { name: "login", templateUrl: 'partials/login.html' })
        .when('/register', { name: "register", templateUrl: 'partials/register.html' })
        .when('/resetPassword', { name: "resetPassword", templateUrl: 'partials/resetPassword.html' })
        .when('/logout', { name: "logout", templateUrl: 'partials/logout.html'})
        .when('/myProfile', { name: "myProfile", templateUrl: 'partials/myProfile.html' })
        .when('/myProfile/changeRemoveCar/:carId', { name: "car:edit", templateUrl: 'partials/changeRemoveCar.html' })
        .when('/contact', { name: "contact", templateUrl: 'partials/contact.html' })
        .when('/about', { name: "about", templateUrl: 'partials/about.html' })
        .when('/addCar', { name: "car:add", templateUrl: 'partials/addCar.html' })
        .when('/addRoute', { name: "route:add", templateUrl: 'partials/addRoute.html' })
        .when('/changePassword', { name: "changePassword", templateUrl: 'partials/changePassword.html' })
        .when('/search', { name: "search", templateUrl: 'partials/search.html'})
        .when("/route/:routeId", { name: "route:view", templateUrl: 'partials/viewRoute.html' })
        .when('/inbox', { name: "inbox", templateUrl: 'partials/inbox.html' })
        .when('/changeRoute/:routeId', { name: "route:edit", templateUrl: 'partials/changeRoute.html' })
        .when('/profile/:userId',{ name: "profile", templateUrl: 'partials/profile.html' })
        .otherwise({ redirectTo: '/login' });
}]);
carpoolingApp.run(function($rootScope, $route, cpaSvcRouteurlsV1) {
	console.log(cpaSvcRouteurlsV1);
    $rootScope.urls = cpaSvcRouteurlsV1;
});
*/
// BEGIN GLOBAL VARIABLES
// Root URL for API calls
var rootUrl = "http://localhost:8080/BackEnd";

// Table paginating: items per page
var itemsPerPage = 1;

// END GLOBAL VARIABLES

//Redefining this in the global scope because someone removed it before removing all references
//and thus quite a lot of shit broke -_-
//var carpoolingControllers = angular.module('carpoolingControllers', ['ui.bootstrap']);
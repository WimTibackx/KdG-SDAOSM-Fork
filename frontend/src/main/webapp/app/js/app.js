//(function() {
	angular.module('cpa', ['ui.router', 'cpa.ctrl']);
	angular.module('cpa').config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
		$urlRouterProvider.otherwise("/login");
		
		var memberNav = {
				templateUrl: "partials/nav.member.html"
		};
		var guestNav = {
				templateUrl: "partials/nav.guest.html"
		}
		
		$stateProvider
			.state("login", {
				url: "/login",
				views: {
					"main@": {
						template: "<h1>login {{foo}}</h1>",
						//templateUrl: "partials/login.html",
						controller: "LoginCtrl",
					},
					"menu@": guestNav
				}
			}).state("register", {
				url: "/register",
				views: {
					"main@": {
						template: "<h1>register {{bar}}</h1>",
						//templateUrl: "partials/register.html",
						controller: "RegisterCtrl"
					},
					"menu@": guestNav
				}
			}).state("myProfile", {
				url: "/myProfile",
				views: {
					"main@": {
						template: "<h1>myProfile {{name}}</h1><div ui-view></div>",
						//templateUrl: "partials/myProfile.html",
						controller: "MyProfileCtrl"
					},
					"menu@": memberNav
				}
			}).state("myProfile.foo", {
				url: "/foo",
				template: "<h2>MyProfileFoo</h2>"
			});
	}]).run(function($rootScope, $state, $stateParams) {
		$rootScope.$state = $state;
		$rootScope.$stateParams = $stateParams;
	});
//})();
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
//var rootUrl = "http://localhost:8080/BackEnd";

// Table paginating: items per page
//var itemsPerPage = 1;

// END GLOBAL VARIABLES

//Redefining this in the global scope because someone removed it before removing all references
//and thus quite a lot of shit broke -_-
//var carpoolingControllers = angular.module('carpoolingControllers', ['ui.bootstrap']);
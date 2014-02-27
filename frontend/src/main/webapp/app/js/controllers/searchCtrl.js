carpoolingControllers.controller('searchCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    console.log("searchCtrl test");

    /*var active = document.getElementById("searchTab");
    active.setAttribute("class", "active");

    var deactivate = document.getElementById("myProfileTab");
    deactivate.setAttribute("class", "");
    */

    var defaultSearches = function(){
        $scope.hideRouteSearch = true;
        $scope.hideTimeSearch = true;
        $scope.hideUserSearch = true;
        $scope.hideResultsSearch = true;
    }

    defaultSearches();
    $scope.hideRouteSearch = false;

    $scope.routeSearchClick = function(){
        defaultSearches();
        $scope.hideRouteSearch = false;
        console.log("click route search");
    }
    $scope.timeSearchClick = function(){
        defaultSearches();
        $scope.hideTimeSearch = false;
        console.log("click time search");
    }
    $scope.userSearchClick = function(){
        defaultSearches();
        $scope.hideUserSearch = false;
        console.log("click user search");
    }
    $scope.resultsSearchClick = function(){
        defaultSearches();
        $scope.hideResultsSearch = false;
        console.log("click results search");
    }
}]);


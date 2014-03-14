carpoolingApp.controllerProvider.register('changeRouteCtrl',['$scope','$http','$location','$routeParams','$authChecker', function ($scope, $http, $location, $routeParams, $authChecker) {
    $authChecker.checkAuthorization();

    $scope.isLoading=true;
    $scope.route={};

    $http.get(rootUrl + "/authorized/route/"+$routeParams.routeId).success(function(data) {
        if (data.hasOwnProperty("error")) {
            if (data.error == "AuthorizationNeeded") {
                $location.path("/login");
                return;
            }
        }

        $scope.route.startDate = new Date(data.beginDate);
        $scope.route.endDate = new Date(data.endDate);
        $scope.route.chauffeur = data.chauffeur;
        $scope.route.chauffeur.dob = new Date($scope.route.chauffeur.dateOfBirth);
        $scope.route.car = data.car;
        $scope.route.weekdayroutes=[];

        for (var i in data.passages) {
            if (i === 'length' || !data.passages.hasOwnProperty(i)) continue;
            var p = {num:i, passages:data.passages[i],markers:[]};
            for (var j=0; j<p.passages.length; j++) {
                var m = {
                    title: p.passages[j].address,
                    lat: p.passages[j].lat,
                    lng: p.passages[j].lng
                };
                p.markers.push(m);
            }
            $scope.route.weekdayroutes.push(p);
        }
    });
}]);
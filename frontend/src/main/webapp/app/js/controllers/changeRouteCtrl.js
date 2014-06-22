carpoolingApp.controllerProvider.register('changeRouteCtrl', ['$scope', '$http', '$location', '$routeParams', function ($scope, $http, $location, $routeParams) {
    $scope.activeTab = 'car';
    $scope.isLoading = true;
    $scope.route = {};
    $scope.times = [];

    var today = new Date();
    $scope.changeDate; // today.getFullYear() + "-" + (today.getMonth()+1) + "-" + today.getDate();

    var jsonObj = {};

    $http.get(rootUrl + "/authorized/route/" + $routeParams.routeId).success(function (data) {
        if (data.hasOwnProperty("error")) {
            if (data.error == "AuthorizationNeeded") {
                $location.path("/login");
                return;
            } else {
                console.log(data.error);
            }
        }

        console.log(data);

        $scope.route.startDate = new Date(data.beginDate);
        $scope.route.endDate = new Date(data.endDate);
        $scope.route.chauffeur = data.chauffeur;
        $scope.route.chauffeur.dob = new Date($scope.route.chauffeur.dateOfBirth);
        $scope.route.car = data.car;
        $scope.route.weekdayroutes = [];

        for (var i in data.passages) {
            if (i === 'length' || !data.passages.hasOwnProperty(i)) continue;
            var p = {num: i, passages: data.passages[i], markers: []};
            for (var j = 0; j < p.passages.length; j++) {
                var m = {
                    title: p.passages[j].address,
                    lat: p.passages[j].lat,
                    lng: p.passages[j].lng
                };
                p.markers.push(m);
            }
            $scope.route.weekdayroutes.push(p);
        }

        console.log("WDR", $scope.route.weekdayroutes);

        $scope.changes = [];
        for (var j = 0; j < 5; j++) {
            $scope.changes.push({ text: "Hello " + j });
        }

        jsonObj.routeId = $routeParams.routeId;
        jsonObj.changes = [];

    }).error(function (status, httpcode) {
            console.log("ERROR", httpcode);
            console.log("STATUS", status);
        });

    $scope.addChange = function (cat, data1, data2) {
        console.log(data1);
        switch (cat) {
            case 'delPT':
                $scope.route.weekdayroutes[data2].passages.splice(data1, 1);
                delObj = {};
                delObj.type = 'deletePlaceTime';
                delObj.weekdayroute = $scope.activeTab;
                delObj.placeTimeId = data1;
                delObj.times = [];

                $scope.route.weekdayroutes[data2].passages.forEach(function (p) {
                    timeObj = {};
                    timeObj.lat = p.lat;
                    timeObj.lng = p.lng;
                    timeObj.time = p.time;
                    delObj.times.push(timeObj);
                });

                jsonObj.changes.push(delObj);
                console.log(jsonObj);

                break;
            case 'addWDR':
                break;
            case 'delWDR':
                break;
            case 'addPT':
                break;
            case 'changeCar':
                break;
            default:
                console.log('default error');
        }

    };

    $scope.submitChanges = function () {
        jsonObj.startDate = $scope.changeDate;

        $scope.route.weekdayroutes.forEach(function(p) {
            changeObj = {};
            changeObj.weekdayroute = p.num;
            changeObj.times = [];
            p.passages.forEach(function(q) {
                timeObj = {};
                timeObj.lat = q.lat;
                timeObj.lng = q.lng;
                timeObj.time = q.time;
                changeObj.times.push(timeObj);
            })
            jsonObj.changes.push(changeObj);
        });

        console.log("FINAL OBJECT", jsonObj);

        $http.post(rootUrl + "/authorized/route/" + $routeParams.routeId + "/change", JSON.stringify(jsonObj))
            .success(function(){
                console.log('Successful change of route');
            });

    };

    $scope.changeTab = function (tab) {
        $scope.activeTab = tab;
        console.log("ActiveTabChanged:", $scope.activeTab);
    };

    $scope.getWeekdayL10n = function (weekdayNum) {
        var weekdaysL10n = ["Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrijdag", "Zaterdag", "Zondag"];
        if (weekdayNum == undefined) return weekdaysL10n[0];
        return weekdaysL10n[weekdayNum];
    };
}]);
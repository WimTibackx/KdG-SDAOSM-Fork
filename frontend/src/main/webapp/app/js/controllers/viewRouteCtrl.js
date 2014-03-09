carpoolingApp.controllerProvider.register('viewRouteCtrl', ['$scope', '$http', '$location', '$routeParams', function ($scope, $http, $location, $routeParams) {
    $scope.isLoading=true;
    $scope.route={};
    var activeTab=0;
    $scope.error={};
    $scope.trajRequest={pickup: undefined, dropoff: undefined};
    $scope.mapInit={
        center: {latitude: 51.219448, longitude: 4.402464},
        zoom: 8
    };

    $http.get(rootUrl + "/authorized/route/"+$routeParams.routeId).success(function(data) {
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

        activeTab = $scope.route.weekdayroutes[0].num;
        console.log("LET THE DEBUGGING COMMENCE.");
        console.log(data);
        console.log($scope.route.weekdayroutes);

        $scope.isLoading=false;
    });

    $scope.activateTab=function(tabNum) {
        activeTab = tabNum;
        $scope.error={};
        $scope.trajRequest={};
    };

    $scope.shouldShowTab=function(tabNum) {
        return tabNum == activeTab;
    };

    // This function is still relevant when we finally implement I18N/L10N,
    //  As it can then translate the gender-data into the correct L10N-key.
    $scope.getGenderL10n=function(gender) {
        switch (gender) {
            case "MALE": return "man";
            default: case "FEMALE": return "vrouw";
        }
    };

    // This function is still relevant when we finally implement I18N/L10N,
    //  As it can then translate the smoker-data into the correct L10N-key.
    $scope.getSmokerL10n=function(smoker) {
        if (smoker) return "roker";
        else return "niet-roker";
    };

    $scope.getFueltypeL10n=function(fueltype) {
        switch (fueltype) {
            case "DIESEL": return "diesel";
            case "SUPER95": return "Super 95";
            default: case "SUPER98": return "Super 98";
        }
    };

    $scope.getDateL10n=function(date) {
        if (date == undefined) return "";
        return date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getFullYear();
    };

    $scope.getWeekdayL10n=function(weekdayNum) {
        var weekdaysL10n=["Maandag","Dinsdag","Woensdag","Donderdag","Vrijdag","Zaterdag","Zondag"];
        if (weekdayNum == undefined) return weekdaysL10n[0];
        return weekdaysL10n[weekdayNum];
    };

    $scope.getAvatarUrl=function(part) {
        return "http://localhost:8080/BackEnd/userImages/"+part;
    };

    $scope.getCarImageUrl=function(part) {
        return "http://localhost:8080/BackEnd/carImages/"+part;
    };

    $scope.requestTraject = function() {
        $scope.error={};
        console.log($scope.trajRequest.pickup,$scope.trajRequest.dropoff);
        if ($scope.trajRequest.pickup == undefined || $scope.trajRequest.dropoff == undefined) {
            $scope.error.bothrequired=true;
            return;
        }
        var pickupP = getPassageWithId($scope.trajRequest.pickup);
        var dropoffP = getPassageWithId($scope.trajRequest.dropoff);
        if (pickupP == undefined || dropoffP != undefined) {
            $scope.error.bothrequired = true; //User has been meddling with the data...
        }
        if (dropoffP.seqnr <= pickupP.seqnr) {
            $scope.error.sequence = true;
            return;
        }
        console.log("All clear");
    };

    function getPassageWithId(id) {
        var relevantPassages=$scope.route.weekdayroutes[activeTab].passages;
        var wanted = undefined;
        for (var i=0; i<relevantPassages.length; i++) {
            if (id == relevantPassages[i].id) {
                wanted = relevantPassages[i];
                break;
            }
        }
        return wanted;
    }
}]);
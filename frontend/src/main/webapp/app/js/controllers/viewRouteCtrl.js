carpoolingApp.controllerProvider.register('viewRouteCtrl', ['$scope', '$http', '$location', '$routeParams', '$authChecker', function ($scope, $http, $location, $routeParams, $authChecker) {
    deleteActiveClass();
    $('#MyProfileTab').addClass('active');

    $authChecker.checkAuthorization();

    $scope.isLoading=true;
    $scope.route={};
    var activeTab=0;
    $scope.error={};
    $scope.trajRequest={pickup: undefined, dropoff: undefined};
    $scope.mapInit={
        center: {latitude: 51.219448, longitude: 4.402464},
        zoom: 8
    };
    $scope.viewTrajects=false;

    $scope.shouldshow={};
    var tabnums = [];

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

        tabnums=[];
        for (var i in data.passages) {
            if (i === 'length' || !data.passages.hasOwnProperty(i)) continue;
            var p = {num:i, passages:data.passages[i],markers:[],trajects:[],trajectsEmpty:true};
            for (var j=0; j<p.passages.length; j++) {
                var m = {
                    title: p.passages[j].address,
                    lat: p.passages[j].lat,
                    lng: p.passages[j].lng
                };
                p.markers.push(m);
            }
            $scope.route.weekdayroutes.push(p);
            tabnums.push(i);
        }

        getTrajectsOnRoute();
        determineActiveTabAtPageload();
        $scope.isLoading=false;
    });

    $scope.activateTab=function(tabNum) {
        activeTab = tabNum;
        $scope.error={};
        $scope.trajRequest={};
        $scope.shouldshow={};
        for (var i=0; i<tabnums.length; i++) {
            $scope.shouldshow[tabnums[i]]=(tabnums[i]==activeTab);
        }
        console.log($scope.shouldshow);
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

    $scope.getAvatarUrl=function(url, gender){
        if (url != null){
            return "http://localhost:8080/BackEnd/userImages/"+url;
        }else{
            if (gender == "FEMALE"){
                return "../app/img/femaleAvatar.png";
            }else {
                return "../app/img/maleAvatar.png";
            }
        }
    };

    $scope.getCarImageUrl=function(part) {
        if (part != null){
            return "http://localhost:8080/BackEnd/carImages/"+part;
        }else{
            return "../app/img/carAvatar.jpg"
        }

    };

    $scope.requestTraject = function() {
        $scope.error={};
        if ($scope.trajRequest.pickup == undefined || $scope.trajRequest.dropoff == undefined) {
            $scope.error.bothrequired=true;
            return;
        }
        var pickupP = getPassageWithId($scope.trajRequest.pickup);
        var dropoffP = getPassageWithId($scope.trajRequest.dropoff);
        if (pickupP == undefined || dropoffP == undefined) {
            $scope.error.bothrequired = true; //User has been meddling with the data...
            return;
        }
        if (dropoffP.seqnr <= pickupP.seqnr) {
            $scope.error.sequence = true;
            return;
        }
        var data=JSON.stringify({pickup:$scope.trajRequest.pickup, dropoff:$scope.trajRequest.dropoff});
        $http.post(rootUrl + "/authorized/route/"+$routeParams.routeId+"/request-traject", data).success(function(response) {
            if (response.hasOwnProperty("status") && response.status == "ok") {
                $location.path("/myProfile").hash("trajects");
            } else if (response.hasOwnProperty("error")) {
                if (response.error == "PlaceTimesOfDifferentRoutes") $scope.error.bothrequired = true;
                else if (response.error == "PlaceTimesOfDifferentWeekdayRoutes") $scope.error.bothrequired = true;
                else if (response.error == "PlaceTimesInWrongSequence") $scope.error.sequence = true;
                else if (response.error == "TrajectNotEnoughCapacity") $scope.error.capacity = true;
                else $scope.error.unknown = true;
            } else {
                $scope.error.unknown = true;
            }
        }).error(function() {
            $scope.error.unknown = true;
        });
    };

    function getPassageWithId(id) {
        var wantedIndex=-1;
        for (var j=0; j<$scope.route.weekdayroutes.length; j++) {
            if (activeTab == $scope.route.weekdayroutes[j].num) {
                wantedIndex=j;
            }
        }
        if (wantedIndex == -1) return undefined;
        var relevantPassages=$scope.route.weekdayroutes[wantedIndex].passages;
        var wanted = undefined;
        for (var i=0; i<relevantPassages.length; i++) {
            if (id == relevantPassages[i].id) {
                wanted = relevantPassages[i];
                break;
            }
        }
        return wanted;
    }

    function determineActiveTabAtPageload() {
        var wantedIndex=-1;
        for (var j=0; j<$scope.route.weekdayroutes.length; j++) {
            if ($location.hash() == $scope.route.weekdayroutes[j].num) {
                wantedIndex=j;
            }
        }
        if (wantedIndex == -1) {
            activeTab = $scope.route.weekdayroutes[0].num;
            $scope.activateTab(activeTab);
            return;
        }
        activeTab = $location.hash();
        $scope.activateTab(activeTab);
    }

    function getTrajectsOnRoute() {
        $http.get(rootUrl+"/authorized/traject/on-route/"+$routeParams.routeId).success(function(data) {
            if (data.hasOwnProperty("error") && data.error == "Unauthorized") {
                //No problem here, user just shouldn't see the info
                return;
            }
            for (var i=0; i<data.length; i++) {
                for (var j=0; j<$scope.route.weekdayroutes.length; j++) {
                    if (data[i].weekday == $scope.route.weekdayroutes[j].num) {
                        $scope.route.weekdayroutes[j].trajects.push(data[i]);
                    }
                    $scope.route.weekdayroutes[j].trajectsEmpty = ($scope.route.weekdayroutes[j].trajects.length == 0);
                }
            }
            $scope.viewTrajects=true;
        });
    }

    $scope.currentPage=0;
    $scope.itemsPerPage = itemsPerPage;
    $scope.numberOfPages = function (data) {
        if (data) {
            result = Math.ceil(data.length / $scope.itemsPerPage);
            return (result > 0) ? result : 1;
        } else {
            return -1;
        }
    };

    $scope.goToProfile=function(userId) {
        $location.path("/profile/"+userId);
    };
}]);
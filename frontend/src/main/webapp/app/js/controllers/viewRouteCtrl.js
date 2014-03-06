carpoolingApp.controllerProvider.register('viewRouteCtrl', ['$scope', '$http', '$location', '$routeParams', function ($scope, $http, $location, $routeParams) {

    console.log("hey test viewRouteCtrl/"+$routeParams.routeId);
    $scope.route={};
    $scope.route.startDate=new Date("2014-02-10");
    $scope.route.endDate=new Date("2014-09-12");
    $scope.route.chauffeur={name:"Ludo van Rosmalen",gender:"MALE",smoker:false,dob:new Date("1971-09-03"),avatar:"TestUser108673684.png"};
    $scope.weekdays=[{num:0,name:"Maandag"},{num:1,name:"Dinsdag"},{num:5,name:"Donderdag"}];
    $scope.passages=[
        {seqnr:1,address:"N14 163-193, 2320 Hoogstraten, Belgium",time:"06:45",lat:51.400110,long:4.760710},
        {seqnr:2,address:"N115 2-30, 2960 Brecht, Belgium",time:"07:03",lat:51.351255,long:4.641555},
        {seqnr:3,address:"Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium",time:"07:17",lat:51.208078,long:4.442945},
        {seqnr:4,address:"N177 100-122, 2850 Boom, Belgium",time:"07:33",lat:51.090334,long:4.365175},
        {seqnr:5,address:"Willebroekkaai 35, 1000 Brussel, Belgium",time:"07:55",lat:50.862557,long:4.352118}
    ];
    $scope.route.car={brand:"Opel",type:"Insignia",fueltype:"DIESEL",consumption:8.3,image:"AudiA5-122764298.jpg"};

    $scope.error={};
    $scope.trajRequest={};

    var activeTab=$scope.weekdays[0].num;

    $scope.activateTab=function(tabNum) {
        activeTab = tabNum;
        $scope.error={};
        initMap();
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
        return date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getFullYear();
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
        if ($scope.trajRequest.pickup == 0 || $scope.trajRequest.dropoff == 0) {
            $scope.error.bothrequired=true;
            return;
        }
        //TODO: EXPLICITLY CHECK IF THEY'RE WITHIN THE SEQUENCE
        if ($scope.trajRequest.dropoff <= $scope.trajRequest.pickup) {
            $scope.error.sequence = true;
            return;
        }
        console.log("All clear");
    };

    /*
     * MAP STUFF.. Will try just using the API,
     *  but there are directives for maps available, you know...
     */

    var mapStuff={};
    mapStuff.markers=[];

    function initMap() {
        mapStuff={};
        mapStuff.markers=[];
        var mapOptions = {
            center: new google.maps.LatLng(51.219448, 4.402464),
            zoom: 8
        };
        mapStuff.directionsService = new google.maps.DirectionsService();
        mapStuff.directionsRenderer = new google.maps.DirectionsRenderer({
            suppressMarkers:true,
            draggable:false
        });
        console.log(document.getElementById("map-canvas-"+activeTab));
        mapStuff.map = new google.maps.Map(document.getElementById("map-canvas-"+activeTab), mapOptions);
        mapStuff.directionsRenderer.setMap(mapStuff.map);
        google.maps.event.addListenerOnce(mapStuff.map, 'idle',displayMarkers);
    }

    function displayMarkers() {
        for (var key in $scope.passages) {
            var passage=$scope.passages[key]; //Right, I forgot about stupid foreach in JS...
            console.log(passage);
            mapStuff.markers.push(new google.maps.Marker({
                map: mapStuff.map,
                title: passage.address,
                position: new google.maps.LatLng(passage.lat, passage.long),
                draggable: false
            }));
            console.log(new google.maps.LatLng(passage.lat, passage.long));
        }
        console.log(mapStuff.markers);
        calcDisplayRoute();
    }

    function calcDisplayRoute() {
        var routepoints=[];
        for (var i=1; i<mapStuff.markers.length-1; i++) {
            routepoints.push({
                location: mapStuff.markers[i].getPosition(),
                stopover:true
            });
        }
        var directionsRequest = {
            origin: mapStuff.markers[0].getPosition(),
            destination: mapStuff.markers[mapStuff.markers.length-1].getPosition(),
            waypoints: routepoints,
            optimizeWaypoints: true,
            travelMode: google.maps.TravelMode.DRIVING
        };
        mapStuff.directionsService.route(directionsRequest, function(result, status) {
            if (status == google.maps.DirectionsStatus.OK) {
                mapStuff.directionsRenderer.setDirections(result);
                mapStuff.map.panToBounds(result.routes[0].bounds);
            } else {
                console.log("Error calculating route: " + status);
            }
        });
    }

    (function() {
        //google.maps.event.addDomListener(window, 'load', initMap);
        //initMap();
    }());
}]);
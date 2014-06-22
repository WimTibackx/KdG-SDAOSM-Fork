var carpoolDirectives = angular.module('carpoolDirectives', []);

carpoolDirectives.directive('ngFileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.ngFileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

carpoolDirectives.directive('cpaDveUserAccessV1', ['cpa.svc.pageaccess.v1', 'cpa.svc.auth.v1', function(cpa_pageaccess, cpa_auth) {
	return {
		restrict: 'A',
		scope: {},
		link: function(scope, element, attrs) {
			cpa_pageaccess.set(attrs.cpaDveUserAccessV1);
			cpa_auth.check();
		}
	};
}]);

carpoolDirectives.directive('cpaDveNavV1', ['cpa.svc.auth.v1', function(cpa_auth) {
	return {
		restrict: 'E',
		scope: {},
		templateUrl: 'partials/nav.html',
		link: function(scope, element, attrs) {
			scope.auth = cpa_auth;
		}
	};
}]);

carpoolDirectives.directive('cpaGmap',function() {
    var defaultMapOptions={
        center: new google.maps.LatLng(51.219448, 4.402464),
        zoom: 8
    };

    function initMap(element, markersData, mapOptions) {
        var mapData={markers:[]};
        mapData.directionsService = new google.maps.DirectionsService();
        mapData.directionsRenderer = new google.maps.DirectionsRenderer({
            suppressMarkers:true,
            draggable:false
        });
        mapData.map = new google.maps.Map(element, mapOptions);
        mapData.directionsRenderer.setMap(mapData.map);
        google.maps.event.addListenerOnce(mapData.map, "idle", function() {
            loadMarkers(markersData, mapData);
            displayRoute(mapData);
        });
    }

    function loadMarkers(markersData, mapData) {
        mapData.markers=[];
        for (var i=0; i<markersData.length; i++) {
            var markerData = markersData[i];
            mapData.markers.push(new google.maps.Marker({
                map: mapData.map,
                title: markerData.title,
                position: new google.maps.LatLng(markerData.lat, markerData.lng),
                draggable: false
            }));
        }
    }

    function displayRoute(mapData) {
        var routepoints=[];
        for (var i=1; i<mapData.markers.length-1; i++) {
            routepoints.push({
                location: mapData.markers[i].getPosition(),
                stopover:true
            });
        }
        var directionsRequest = {
            origin: mapData.markers[0].getPosition(),
            destination: mapData.markers[mapData.markers.length-1].getPosition(),
            waypoints: routepoints,
            optimizeWaypoints: true,
            travelMode: google.maps.TravelMode.DRIVING
        };
        mapData.directionsService.route(directionsRequest, function(result, status) {
            if (status == google.maps.DirectionsStatus.OK) {
                mapData.directionsRenderer.setDirections(result);
                console.log("panning to",result.routes[0].bounds);
                mapData.map.panToBounds(result.routes[0].bounds);
            } else {
            }
        });
    }

    function link(scope, element, attrs) {
        initMap(element[0],scope.markers,defaultMapOptions);

        attrs.$observe('isshown', function(value) {

            console.log("attr changed, is now"+value/*,scope.isshown*/);

            initMap(element[0],scope.markers,defaultMapOptions);
        });

    }

    return {
        restrict: 'E',
        scope: {
            markers:"=markers"//,
            //isshown:"=isshown"
        },
        link: link
    };
});

var map;
var txtStart, txtEnd;
var autocomplete = [];
var autoStart, autoEnd;
var markers = [];
var geoCoder;

var directionsDisplay;
var directionsService;

var points;

carpoolingControllers.controller('searchCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    console.log("searchCtrl test");

    /*var active = document.getElementById("searchTab");
     active.setAttribute("class", "active");

     var deactivate = document.getElementById("myProfileTab");
     deactivate.setAttribute("class", "");
     */

    var defaultSearches = function () {
        $scope.hideRouteSearch = true;
        $scope.hideTimeSearch = true;
        $scope.hideUserSearch = true;
        $scope.hideResultsSearch = true;
    }

    var initializeMap = function () {
        //addListeners();
        // Creating new Geocoder (for turning coordinates into human-readable addresses)
        geoCoder = new google.maps.Geocoder();

        // Creating directionsService
        directionsService = new google.maps.DirectionsService();
        directionsDisplay = new google.maps.DirectionsRenderer({
            suppressMarkers: true,
            draggable: true
        });

        //map options (latlng = value to show map)
        var mapOptions = {
            center: new google.maps.LatLng(51.219448, 4.402464),
            zoom: 8
        };
        // mapOption: zoomlevel, middelpunt bepalen, ...
        // https://developers.google.com/maps/documentation/javascript/reference#MapOptions

        //create map with defined options
        map = new google.maps.Map(document.getElementById('map-canvasSearch'),
            mapOptions);
        directionsDisplay.setMap(map);

        //auto complete
        // Get the HTML control elements and store them in variables because we will need them later.
        txtStart = document.getElementById('txtStart');
        txtEnd = document.getElementById('txtEnd');

        // Hide second box and button
        txtEnd.style.display = 'none';

        map.controls[google.maps.ControlPosition.TOP_LEFT].push(document.getElementById('controls'));
        //create autocomplete object

        autoStart = new google.maps.places.Autocomplete(txtStart);
        autoEnd = new google.maps.places.Autocomplete(txtEnd);

        autocomplete[0] = autoStart;
        autocomplete[1] = autoEnd;

        //add listener to box
        google.maps.event.addListener(autoStart, 'place_changed', onPlaceChanged);
        google.maps.event.addListener(autoEnd, 'place_changed', onPlaceChanged);

        google.maps.event.addListenerOnce(map, 'idle', function () {
            // Do something only the first time the map is loaded
            txtStart.focus();
        });
    }

    var onPlaceChanged = function () {
        var step = autocomplete.indexOf(this);
        var place = this.getPlace();
        if (place.geometry) {
            map.panTo(place.geometry.location);
            map.setZoom(15);
            var icon = 'img/map/marker' + (step + 1) + '.png';
            var marker = new google.maps.Marker({
                map: map,
                title: place.formatted_address,
                position: place.geometry.location,
                draggable: true,
                icon: icon
            });
            google.maps.event.addListener(marker, 'dragend', function () {
                updateAddress(step, this.getPosition());
            });

            if (markers[step] != null) {
                markers[step].setMap(null);
            }
            markers[step] = marker;

            if (markers[0]) {
                txtEnd.style.display = 'block';
                txtEnd.focus();
            }
            if (markers[1]) {
                //btnSend.style.display = 'block';
            }
            calcRoute();
        }
    }

    var updateAddress = function (step, latlng) {
        geoCoder.geocode({ location: latlng }, function (result, status) {
            if (status == "OK") {
                /* console.log('------------');
                 for (var i = 0; i < result.length; i++) {
                 console.log(result[i].formatted_address);
                 } */
                //change marker title
                markers[step].setTitle(result[0].formatted_address);
                if (step == 0) {
                    txtStart.value = result[0].formatted_address;
                } else {
                    txtEnd.value = result[0].formatted_address;
                }
                calcRoute();
            } else {
                console.log("Error decoding address: " + status);
                setTimeout(function () {
                    updateAddress(step, latlng);
                }, 1000);
            }
        })
    }

    var calcRoute = function () {
        if (markers[1]) {
            var request = {
                origin: markers[0].getPosition(),
                destination: markers[1].getPosition(),
                travelMode: google.maps.TravelMode.DRIVING
            };
            directionsService.route(request, function (result, status) {
                if (status == google.maps.DirectionsStatus.OK) {
                    directionsDisplay.setDirections(result);
                    var routepoints = result.routes[0].overview_path;
                    for (i = 0; i < routepoints.length; i++) {
                        new google.maps.Marker({
                            map: map,
                            position: routepoints[i]
                        });
                    }
                } else {
                    console.log("Error calculating route: " + status);
                    setTimeout(function () {
                        calcRoute();
                    }, 1000);
                }
            });
        }
    }

    defaultSearches();
    $scope.hideRouteSearch = false;
    initializeMap();

    $scope.routeSearchClick = function () {
        defaultSearches();

        $scope.hideRouteSearch = false;
        console.log("click route search");

        //initializeMap();
    }
    $scope.timeSearchClick = function () {
        defaultSearches();
        $scope.hideTimeSearch = false;
        console.log("click time search");

        /* search time field */
        $scope.today = function () {
            $scope.dt = new Date();
        };
        $scope.today();

        $scope.showWeeks = true;

        // Disable weekend selection
        $scope.disabled = function (date, mode) {
            return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
        };

        $scope.toggleMin = function () {
            $scope.minDate = ( $scope.minDate ) ? null : new Date();
        };
        $scope.toggleMin();

        /* time picker */
        $scope.mytime = new Date();

        $scope.hstep = 1;
        $scope.mstep = 1;

        /*$scope.options = {
         hstep: [1, 2, 3],
         mstep: [1, 5, 10, 15, 25, 30]
         };*/

        $scope.ismeridian = true;
        $scope.toggleMode = function () {
            $scope.ismeridian = !$scope.ismeridian;
        };
        $scope.isRadius = false;
        $scope.toggleMode = function () {
            $scope.isRadius = !$scope.isRadius;
        };

        $scope.update = function () {
            var d = new Date();
            d.setHours(14);
            d.setMinutes(0);
            $scope.mytime = d;
        };

        $scope.changed = function () {
            console.log('Time changed to: ' + $scope.mytime);
        };

        $scope.hours = new Date().getHours();
        $scope.minutes = new Date().getMinutes();
    }
    $scope.userSearchClick = function () {
        defaultSearches();
        $scope.hideUserSearch = false;
        console.log("click user search");
    }
    $scope.resultsSearchClick = function () {
        defaultSearches();
        $scope.hideResultsSearch = false;
        console.log("click results search");

        //get results
        //get date
        var date = $scope.dt.toISOString().split("T")[0];
        //var day = date.getDay();
        //var month = date.getMonth();
        //var year = date.getYear();
        console.log(date);
        //get time
        /*var hours = $scope.mytime.getHours();
         var minutes = $scope.mytime.getMinutes();
         console.log(hours + ":" + minutes);
         //get radius
         var rHours = $scope.mytimeRadius.getHours();
         var rMinutes = $scope.mytimeRadius.getMinutes();
         console.log(rHours + ":" + rMinutes);   */
    }
}]);


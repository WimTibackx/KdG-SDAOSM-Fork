var map;
var txtStart, txtEnd;
var autocomplete = [];
var autoStart, autoEnd;
var markers = [];
var circles = [];

var geoCoder;

var directionsDisplay;
var directionsService;

var points = [];

var datetimeClicked = false;
var userClicked = false;

var smoker = false;
var gender = true;
carpoolingApp.controllerProvider.register('searchCtrl', ['$scope', '$http', '$location', '$authChecker', function ($scope, $http, $location, $authChecker) {

    deleteActiveClass();
    $('#SearchTab').addClass('active');

    $authChecker.checkAuthorization();

    $scope.itemsPerPage = itemsPerPage;
    $scope.currentPage = 0;

    /*var active = document.getElementById("searchTab");
     active.setAttribute("class", "active");

     var deactivate = document.getElementById("myProfileTab");
     deactivate.setAttribute("class", "");
     */

    $scope.showSearchImage = function () {
        return(($scope.modelStart != undefined) && ($scope.modelEnd != undefined) && userClicked && datetimeClicked);
    };

    $scope.selectSmoker = function (s) {
        smoker = s;
    }
    $scope.setNoSmokerClass = function () {
        return !smoker;
    }
    $scope.setSmokerClass = function () {
        return smoker;
    }
    $scope.selectGender = function(g){
        gender = g;
    }
    $scope.setMaleClass = function(){
        return gender;
    }
    $scope.setFemaleClass = function(){
        return !gender;
    }

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

        $scope.radiusChanged = function () {

            for (var i = 0; i < circles.length; i++) {
                circles[i].setMap(null);
            }
            circles = [];

            for (var i = 0; i < markers.length; i++) {
                createCircle(markers[i], i);
            }

           }
    }

    var onPlaceChanged = function () {
        var step = autocomplete.indexOf(this);
        var place = this.getPlace();
        if (place.geometry) {
            map.panTo(place.geometry.location);
            map.setZoom(10);


            var marker = createMarker(place, step);
            createCircle(marker, step);

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
            }
            calcRoute();
        }
    }

    var createMarker = function (place, step) {
        var icon = 'img/map/marker' + (step + 1) + '.png';
        var marker = new google.maps.Marker({
            map: map,
            title: place.formatted_address,
            position: place.geometry.location,
            draggable: true,
            icon: icon
        });
        return marker;
    }

    var createCircle = function (marker, step) {
        var radius = (parseInt(document.getElementById("radiusValue").value)) / 1.6093 * 1000 // /1.6093 to go to miles ==> *1000 for metres in miles;
        var circle = new google.maps.Circle({
            map: map,
            radius: radius,
            fillColor: '#AA0000'
        });
        circle.bindTo('center', marker, 'position');
        circles[step] = circle;
    }

    var updateAddress = function (step, latlng) {
        geoCoder.geocode({ location: latlng }, function (result, status) {
            if (status == "OK") {

                //change marker title
                markers[step].setTitle(result[0].formatted_address);
                if (step == 0) {
                    txtStart.value = result[0].formatted_address;
                } else {
                    txtEnd.value = result[0].formatted_address;
                }
                calcRoute();
            } else {

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

                } else {

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

    }
    $scope.timeSearchClick = function () {
        defaultSearches();
        datetimeClicked = true;
        $scope.hideTimeSearch = false;

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
        $scope.mytimeRadius = new Date(0, 0, 0, 0, 0);

        $scope.hstep = 1;
        $scope.mstep = 1;


        $scope.ismeridian = true;
        $scope.toggleMode = function () {
            $scope.ismeridian = !$scope.ismeridian;
        };
        $scope.isRadius = false;
        $scope.toggleMode = function () {
            $scope.isRadius = !$scope.isRadius;
        };

    }
    $scope.userSearchClick = function () {
        defaultSearches();
        userClicked = true;
        $scope.hideUserSearch = false;
    }
    $scope.resultsSearchClick = function () {
        defaultSearches();
        $scope.hideResultsSearch = false;

        //make json file to send
        var jsonObject = {};

        //get results
        //get markers + radius
        var startLat = markers[0].position.k;
        var startLng = markers[0].position.A;
        var startTitle = markers[0].title;
        var endLat = markers[1].position.k;
        var endLng = markers[1].position.A;
        var endTitle = markers[1].title;
        jsonObject.start = {};
        jsonObject.end = {};
        jsonObject.start.lat = startLat;
        jsonObject.start.lng = startLng;
        jsonObject.start.title = startTitle;
        jsonObject.end.lat = endLat;
        jsonObject.end.lng = endLng;
        jsonObject.end.title = endTitle;

        var radius = parseInt(document.getElementById("radiusValue").value);
        jsonObject.radius = radius;
        //get date
        var date = $scope.dt.toISOString().split("T")[0];
        jsonObject.date = date;
        //get time departure
        var hours = $scope.mytime.getHours();
        var minutes = $scope.mytime.getMinutes();
        jsonObject.hours = hours;
        jsonObject.minutes = minutes;
        //get time radius
        var radiusH = $scope.mytimeRadius.getHours();
        var radiusM = $scope.mytimeRadius.getMinutes();
        jsonObject.radiusHours = radiusH;
        jsonObject.radiusMinutes = radiusM;
        //get smoker
        jsonObject.smoker = smoker;

        //get gender
        if(gender){
            jsonObject.gender = "male";
        }else{
            jsonObject.gender = "female";
        }



        $http.post(rootUrl + "/authorized/route/findCarpoolers", JSON.stringify(jsonObject))
            .success(function (response) {
                $scope.routes=response;
        }).
            error(function () {
            });
    }

    $scope.goRoute = function(id,day) { $location.path("/route/"+id).hash(day); };
    $scope.goAddRoute = function() { $location.path("/addRoute"); };

    $scope.formatDate = function(date) {
        if (date == undefined) return "";
        return date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getFullYear();
    };
    $scope.formatDateString = function(dateString) {
        var date = new Date(dateString);
        return $scope.formatDate(date);
    };
    $scope.booleanL10n = function(b) { return b ? "Ja" : "Nee"; };

    $scope.numberOfPages = function (data) {
        if (data) {
            result = Math.ceil(data.length / $scope.itemsPerPage);
            return (result > 0) ? result : 1;
        } else {
            return -1;
        }
    }
}]);


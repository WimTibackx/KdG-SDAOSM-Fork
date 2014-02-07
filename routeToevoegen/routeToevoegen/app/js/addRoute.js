var map;
var inputStart, inputEnd;
var autocomplete = [];
var autoStart, autoEnd;
var markers = [];
var geoCoder;

var directionsDisplay;
var directionsService;

var send;
function initialize() {
    // Creating new Geocoder
    geoCoder = new google.maps.Geocoder();
    //creating directionsService
    directionsService = new google.maps.DirectionsService();
    directionsDisplay = new google.maps.DirectionsRenderer({suppressMarkers: true, draggable: true});

    //map options (latlng = value to show map)
    var mapOptions = {
        center: new google.maps.LatLng(51.219448, 4.402464),
        zoom: 8
    };
    // mapOption: zoomlevel, middelpunt bepalen, ...
    // https://developers.google.com/maps/documentation/javascript/reference#MapOptions

    //create map with defined options
    map = new google.maps.Map(document.getElementById("map-canvas"),
        mapOptions);
    directionsDisplay.setMap(map);

    //auto complete
    //get the html input element for the autocomplete search box
    inputStart = document.getElementById("txtStart");
    inputEnd = document.getElementById("txtEnd");
    inputEnd.style.display = 'none';

    //get send button
    send = document.getElementById('send');

    map.controls[google.maps.ControlPosition.TOP_LEFT].push(document.getElementById('controls'));
    //create autocomplete object

    autoStart = new google.maps.places.Autocomplete(inputStart);
    autoEnd = new google.maps.places.Autocomplete(inputEnd);

    autocomplete[0] = autoStart;
    autocomplete[1] = autoEnd;

    //add listener to box
    google.maps.event.addListener(autoStart, 'place_changed', onPlaceChanged);
    google.maps.event.addListener(autoEnd, 'place_changed', onPlaceChanged);
    google.maps.event.addDomListener(send, 'click', sendRoute);

    inputStart.focus();
}

function onPlaceChanged() {
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
            inputEnd.style.display = 'block';
        }
        if (markers[1]) {
            // TODO: display button
        }
        calcRoute();
    }
}

function updateAddress(step, latlng) {
    geoCoder.geocode({ location: latlng }, function (result, status) {
        if (status == "OK") {
            console.log('------------');
            for (var i = 0; i < result.length; i++) {
                console.log(result[i].formatted_address);
            }
            //change marker title
            markers[step].setTitle(result[0].formatted_address);
            if (step == 0) {
                inputStart.value = result[0].formatted_address;
            } else {
                inputEnd.value = result[0].formatted_address;
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

function calcRoute() {
    if (markers[1]) {
        var request = {
            origin: markers[0].getPosition(),
            destination: markers[1].getPosition(),
            travelMode: google.maps.TravelMode.DRIVING
        };
        directionsService.route(request, function (result, status) {
            if (status == google.maps.DirectionsStatus.OK) {
                directionsDisplay.setDirections(result);
            }else{
                console.log("Error calculating route: " + status);
                setTimeout(function(){
                    calcRoute();
                },1000);
            }
        });
    }
}

function sendRoute() {
    var points = new Object();

    points.start = new Object();
    points.start.lat = markers[0].getPosition().lat();
    points.start.long = markers[0].getPosition().lng();
    points.start.address = markers[0].getTitle();

    points.end = new Object();
    points.end.lat = markers[1].getPosition().lat();
    points.end.long = markers[1].getPosition().lng();
    points.end.address = markers[1].getTitle();

    console.log(JSON.stringify(points));
}

// Listener
google.maps.event.addDomListener(window, 'load', initialize);

var map;
var mapBounds;
var infoWindow, geoCoder;
var markers = [];

// var callnr = 0;




function initialize() {
    geoCoder = new google.maps.Geocoder();
    // Geocoder: omzetten van adressen naar coordinaten

    var mapOptions = {
        center: new google.maps.LatLng(51.219448, 4.402464),
        zoom: 8
    };
    // mapOption: zoomlevel, middelpunt bepalen, ...
    // https://developers.google.com/maps/documentation/javascript/reference#MapOptions

    map = new google.maps.Map(document.getElementById("map-canvas"),
        mapOptions);
    // Nieuwe map aanmaken in een HTML-element met mapOptions

    mapBounds = new google.maps.LatLngBounds(
    new google.maps.LatLng(-33.8902, 151.1759),
        new google.maps.LatLng(-33.8474, 151.2631));

    var options = {bounds: mapBounds};

    addListeners();

    //auto complete
    //get the thml input element for the autocomplete search box
    var input = document.getElementById("pac-input");
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
    //create autocomplete object
    var autocomplete = new google.maps.places.Autocomplete(input, options);
}

function getLocation(point, callback) {
    var markerLocation;
    if (point.coords.lat && point.coords.lng) {
        markerLocation = new google.maps.LatLng(point.coords.lat, point.coords.lng);
        callback(markerLocation);
    } else {
        // using address to coordinate conversion
        decodeAddress(point, callback);
    }
}

// Geocoder gebruiken om adres om te zetten naar LatLng
function decodeAddress(point, callback) {
    // console.log("calling " + ++callnr);
    geoCoder.geocode({ address: point.city }, function (result, status) {
        if (status == "OK") {
            point.coords.lat = result[0].geometry.location.lat(); // 13 calls with recording of result
            point.coords.lng = result[0].geometry.location.lng(); // 20 calls without (OVER_QUERY_LIMIT; limit: 1 req/user/sec)
            callback(result[0].geometry.location);
        } else {
            console.log("Error decoding address: " + status);
            setTimeout(function () {
                decodeAddress(point, callback)
            }, 1000);
        }
    })
}

google.maps.event.addDomListener(window, 'load', initialize);
// Listener